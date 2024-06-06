package com.java08.quanlituyendung.converter;

import com.java08.quanlituyendung.dto.test.*;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.Test.MulQuestionEntity;
import com.java08.quanlituyendung.entity.Test.MulQuestionOptionEntity;
import com.java08.quanlituyendung.entity.Test.TestEntity;
import com.java08.quanlituyendung.entity.Test.TestRecordEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestConverter {

    @Autowired
    TestRepository testRepository;

    public TestResponseDTO toDTO(TestEntity testEntity) {
        List<MulQuestionResponseDTO> questions = new ArrayList<>();
        testEntity.getMulQuestions().forEach(question -> {
            questions.add(toMulQuestionDto(question));
        });
        TestResponseDTO testResponseDTO = new TestResponseDTO();
        testResponseDTO.setId(testEntity.getId());
        testResponseDTO.setJdId(testEntity.getJobPostingEntity().getId());
        testResponseDTO.setJob(testEntity.getJobPostingEntity().getName());
        testResponseDTO.setSummary(testEntity.getSummary());
        testResponseDTO.setQuestions(questions);
        testResponseDTO.setTime(testEntity.getTime());
        testResponseDTO.setEssayQuestion(testEntity.getEssayQuestion());
        testResponseDTO.setType(testEntity.getType());
        return testResponseDTO;
    }


    public OptionResponseDTO toOptionDto(MulQuestionOptionEntity option) {
        return OptionResponseDTO.builder()
                .id(option.getId())
                .optionText(option.getOptionText())
                .isAnswer(option.isAnswer())
                .build();
    }

    public MulQuestionResponseDTO toMulQuestionDto(MulQuestionEntity question) {
        List<OptionResponseDTO> options = new ArrayList<>();
        question.getOptions().forEach(option -> {
            options.add(toOptionDto(option));
        });

        return MulQuestionResponseDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .options(options)
                .build();
    }


    // fix ok convert cho trac nghiem
    public TestEntity toEntity(NewTestDTO request, JobPostingEntity job) {
//        List<CVEntity> cvs = job.getCvEntities();
//        List<String> attendees = cvs.stream().map(cvEntity -> {
//            return cvEntity.getUserAccountEntity().getEmail();
//        }).collect(Collectors.toList());
        return TestEntity.builder()
                .mulQuestions(new ArrayList<>())
                .jobPostingEntity(job)
                .time(Math.toIntExact(request.getTime()))
                .summary(request.getSummary())
                .type(TestEntity.Type.MULTIPLE_CHOICE)
                .isDelete(false)
                .build();
    }

    //convert cho test tu luan
    public TestEntity toEntityEssay(NewTestDTO request, JobPostingEntity jobPostingEntity) {
        return TestEntity.builder()
                .mulQuestions(new ArrayList<>())
                .jobPostingEntity(jobPostingEntity)
                .time(Math.toIntExact(request.getTime()))
                .summary(request.getSummary())
                .type(TestEntity.Type.ESSAY)
                .essayQuestion(request.getEssayQuestion())
                .isDelete(false)
                .build();
    }
    public TestEntity toEntityCode(NewTestDTO request, JobPostingEntity jobPostingEntity) {
        return TestEntity.builder()
                .mulQuestions(new ArrayList<>())
                .jobPostingEntity(jobPostingEntity)
                .time(Math.toIntExact(request.getTime()))
                .summary(request.getSummary())
                .type(TestEntity.Type.CODE)
                .essayQuestion(request.getEssayQuestion())
                .isDelete(false)
                .build();
    }

    public MulQuestionOptionEntity optionToEntity(AddQuesitonOptionDTO dto, MulQuestionEntity question) {
        return MulQuestionOptionEntity.builder()
                .optionText(dto.getOptionText())
                .question(question)
                .isAnswer(dto.isAnswer())
                .build();
    }


    // hoan thanh cai da bat dau
    public void toRecordEntity(RecordRequestDTO dto, UserAccountEntity user, TestEntity test) {
        List<TestRecordEntity> records = test.getRecords();
        TestRecordEntity record = records.stream()
                .filter(r -> r.getUserAccountEntity().equals(user))
                .findAny()
                .orElse(null);
        if (record != null) {
            record.setRecord(dto.getRecord());
            record.setScore(dto.getScore());
            record.setIsDone(true);
            testRepository.save(test);
        } else {
            test.getRecords().add(TestRecordEntity.builder()
                    .record(dto.getRecord())
                    .score(dto.getScore())
                    .isDone(true)
                    .userAccountEntity(user)
                    .testEntity(test)
                    .build());
            testRepository.save(test);
        }
    }

    // bat dau va chua hoan thanh
    public TestRecordEntity toStartRecordEntity(StartRecordRequestDTO dto, UserAccountEntity user, TestEntity test) {
        return TestRecordEntity.builder()
                .startTime(dto.getStartTime())
                .isDone(false)
                .userAccountEntity(user)
                .testEntity(test)
                .build();
    }

    // dung cho ung vien get
    public TestResponseDTO toDTOcandidate(TestEntity testEntity, UserAccountEntity user) {
        List<MulQuestionResponseDTO> questions = new ArrayList<>();
        testEntity.getMulQuestions().forEach(question -> {
            questions.add(toMulQuestionDto(question));
        });
        TestResponseDTO testResponseDTO = new TestResponseDTO();
        testResponseDTO.setId(testEntity.getId());
        testResponseDTO.setJdId(testEntity.getJobPostingEntity().getId());
        testResponseDTO.setJob(testEntity.getJobPostingEntity().getName());
        testResponseDTO.setSummary(testEntity.getSummary());
        testResponseDTO.setQuestions(questions);
        testResponseDTO.setTime(testEntity.getTime());
        testResponseDTO.setType(testEntity.getType());
        testResponseDTO.setEssayQuestion(testEntity.getEssayQuestion());
        List<TestRecordEntity> records = testEntity.getRecords();
        records.forEach(testRecordEntity -> {
            if (testRecordEntity.getUserAccountEntity().equals(user)) {
                testResponseDTO.setStart(true);
                if(testRecordEntity.getIsDone()==true) {
                    testResponseDTO.setRecord(true);
                } else {
                    testResponseDTO.setStartTime(testRecordEntity.getStartTime());
                    testResponseDTO.setRecord(false);
                }
            } else {
                testResponseDTO.setStart(false);
                testResponseDTO.setRecord(false);
            }
        });
        return testResponseDTO;
    }



}
