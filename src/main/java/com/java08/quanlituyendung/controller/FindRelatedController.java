package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.JobPostingDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.repository.JobPostingRepository;
import com.java08.quanlituyendung.converter.JobPostingConverter;
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
import org.springframework.http.ResponseEntity;
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

    private JobPostingEntity findAdditionalJob(Map<JobPostingEntity, Double> jobScores, List<JobPostingEntity> sortedRelatedJobs) {
        for (JobPostingEntity job : jobPostingRepository.findAll()) {
            if (!sortedRelatedJobs.contains(job)) {
                return job;
            }
        }
        return null;
    }
}
