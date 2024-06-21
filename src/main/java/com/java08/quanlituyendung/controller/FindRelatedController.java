package com.java08.quanlituyendung.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.JobPostingDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.sample.ResumeJsonEntity;
import com.java08.quanlituyendung.repository.JobPostingRepository;
import com.java08.quanlituyendung.converter.JobPostingConverter;
import com.java08.quanlituyendung.repository.ResumeJsonRepository;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/find-related")
public class FindRelatedController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobPostingConverter jobPostingConverter;

    @Autowired
    private UserAccountRetriever userAccountRetriever;

    @Autowired
    private ResumeJsonRepository resumeJsonRepository;

    @PostMapping("/find-related-job")
    public ResponseEntity<ResponseObject> findRelatedJob(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        List<JobPostingEntity> jobPostingEntityList = jobPostingRepository.findAll();
        try {
            Directory directory = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            for (JobPostingEntity job : jobPostingEntityList) {
                Document doc = new Document();
                doc.add(new TextField("name", job.getName(), TextField.Store.YES));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
            QueryParser parser = new QueryParser("name", analyzer);
            Query query = parser.parse(keyword);
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            TopDocs topDocs = indexSearcher.search(query, jobPostingEntityList.size());
            Map<JobPostingEntity, Double> jobScores = new HashMap<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);
                String jobName = doc.get("name");
                Optional<JobPostingEntity> matchedJob = jobPostingEntityList.stream()
                        .filter(job -> job.getName().equals(jobName))
                        .findFirst();
                matchedJob.ifPresent(job -> jobScores.put(job, (double) scoreDoc.score));
            }
            indexReader.close();
            directory.close();
            List<JobPostingEntity> sortedRelatedJobs = jobScores.entrySet().stream()
                    .distinct()
                    .sorted(Map.Entry.<JobPostingEntity, Double>comparingByValue().reversed())
                    .limit(12)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            while (sortedRelatedJobs.size() < 12) {
                JobPostingEntity additionalJob = findAdditionalJob(jobScores, sortedRelatedJobs);
                if (additionalJob != null) {
                    sortedRelatedJobs.add(additionalJob);
                } else {
                    break;
                }
            }
            ResponseObject responseObject = new ResponseObject("success", "Related jobs found",
                    sortedRelatedJobs.stream().map(jobPostingConverter::toDTO).collect(Collectors.toList()));
            return ResponseEntity.ok(responseObject);
        } catch (Exception e) {
            List<JobPostingEntity> fallbackJobList = jobPostingEntityList
                    .stream().limit(12).collect(Collectors.toList());
            List<JobPostingDTO> fallbackJobDTOs = fallbackJobList
                    .stream().map(jobPostingConverter::toDTO).collect(Collectors.toList());
            ResponseObject responseObject = new ResponseObject("error", e.getMessage(), fallbackJobDTOs);
            return ResponseEntity.ok(responseObject);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getRelatedJobByUser(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user == null) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.NOT_ACCEPTABLE.toString())
                    .message("Something went wrong")
                            .data("")
                    .build());
        }
        var resume = resumeJsonRepository.findByUserAccountEntity(user);
        if (resume.isEmpty()) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.NOT_ACCEPTABLE.toString())
                    .message("Something went wrong")
                    .build());
        } else {
            String resumeJson = resume.get().getJsonResume();
            String positionApplied = "";
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(resumeJson);
                positionApplied = rootNode.path("applicationPosition").asText();
                if (positionApplied == null || positionApplied.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseObject("error", "No position applied found in resume", ""));
                }
                List<JobPostingEntity> jobPostingEntityList = findRelatedJobs(positionApplied);
                return ResponseEntity.ok
                        (new ResponseObject("success", positionApplied, jobPostingEntityList));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseObject("error", "Error parsing resume JSON", ""));
            }
        }
    }

    private JobPostingEntity findAdditionalJob(Map<JobPostingEntity, Double> jobScores, List<JobPostingEntity> sortedRelatedJobs) {
        for (JobPostingEntity job : jobPostingRepository.findAll()) {
            if (!sortedRelatedJobs.contains(job)) {
                return job;
            }
        }
        return null;
    }

    public List<JobPostingEntity> findRelatedJobs(String keyword) {
        List<JobPostingEntity> jobPostingEntityList = jobPostingRepository.findAll();
        List<JobPostingEntity> relatedJobs = new ArrayList<>();
        try {
            Directory directory = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);

            for (JobPostingEntity job : jobPostingEntityList) {
                Document doc = new Document();
                doc.add(new TextField("name", job.getName(), TextField.Store.YES));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
            QueryParser parser = new QueryParser("name", analyzer);
            Query query = parser.parse(keyword);
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            TopDocs topDocs = indexSearcher.search(query, jobPostingEntityList.size());
            Map<JobPostingEntity, Double> jobScores = new HashMap<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = indexSearcher.doc(scoreDoc.doc);
                String jobName = doc.get("name");
                Optional<JobPostingEntity> matchedJob = jobPostingEntityList.stream()
                        .filter(job -> job.getName().equals(jobName))
                        .findFirst();
                matchedJob.ifPresent(job -> jobScores.put(job, (double) scoreDoc.score));
            }
            indexReader.close();
            directory.close();

            relatedJobs = jobScores.entrySet().stream()
                    .distinct()
                    .sorted(Map.Entry.<JobPostingEntity, Double>comparingByValue().reversed())
                    .limit(4)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            while (relatedJobs.size() < 4) {
                JobPostingEntity additionalJob = findAdditionalJob(jobScores, relatedJobs);
                if (additionalJob != null) {
                    relatedJobs.add(additionalJob);
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            relatedJobs = jobPostingEntityList.stream().limit(4).collect(Collectors.toList());
        }
        return relatedJobs;
    }
}
