package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.TestConverter;
import com.java08.quanlituyendung.dto.CvDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.TestResultDTO.CodeTestResultResponseDTO;
import com.java08.quanlituyendung.dto.UserAccountPayload.UserAccountCustomResponseDTO;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.dto.test.AddQuestionDTO;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.Test.*;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.*;
import com.java08.quanlituyendung.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeQuestionService {
    private final CodeQuestionRepository codeQuestionRepository;
    private final UserAccountRetriever userAccountRetriever;
    private final MulQuestionRepository mulQuestionRepository;
    private final TestRepository testRepository;
    private final JobPostingRepository jobPostingRepository;
    private final TestConverter testConverter;
    private final MulOptionRepository mulOptionRepository;
    private final RecordRepository recordRepository;
    private final CvRepository cvRepository;

    @Autowired
    public CodeQuestionService(UserAccountRetriever userAccountRetriever
            , MulQuestionRepository mulQuestionRepository
            , JobPostingRepository jobPostingRepository
            , TestConverter testConverter
            , MulOptionRepository mulOptionRepository
            , RecordRepository recordRepository
            , TestRepository testRepository
            , CodeQuestionRepository codeQuestionRepository
            , CvRepository cvRepository) {
        this.userAccountRetriever = userAccountRetriever;
        this.mulQuestionRepository = mulQuestionRepository;
        this.testRepository = testRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.testConverter = testConverter;
        this.mulOptionRepository = mulOptionRepository;
        this.recordRepository = recordRepository;
        this.cvRepository = cvRepository;
        this.codeQuestionRepository = codeQuestionRepository;
    }

    public ResponseEntity<ResponseObject> addQuestion(Authentication authentication, AddCodeQuestion request) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        Optional<TestEntity> optionalTest = testRepository.findById(request.getTestId());

        if (user == null || optionalTest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(Constant.FAIL)
                            .message("Failed to add question: User not found or Test not found")
                            .build());
        } else {
            CodeQuestionEntity newQuestion = CodeQuestionEntity.builder()
                    .questionText(request.getQuestionText())
                    .value(request.getValue())
                    .language(request.getLanguage())
                    .testCase(request.getTestCase())
                    .testFunction(request.getTestFunction())
                    .build();

            TestEntity test = optionalTest.get();
            test.getCodeQuestions().add(newQuestion);
            testRepository.save(test);
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .data(test)
                    .message("Question added successfully")
                    .build());
        }
    }

    public ResponseEntity<ResponseObject> getCodeQuestionByTestId(Long testId, Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        Optional<TestEntity> optionalTest = testRepository.findById(testId);
        if (user == null || optionalTest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(Constant.FAIL)
                            .message("Không thể tìm thấy testId")
                            .build());
        } else {
            List<CodeQuestionEntity> codeQuestions = optionalTest.get().getCodeQuestions();
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .data(codeQuestions)
                    .message("Success!")
                    .build());
        }
    }

    public ResponseEntity<ResponseObject> deleteCodeQuestion(Long codeQuestionId, Authentication authentication) {
        Optional<CodeQuestionEntity> codeQuestion = codeQuestionRepository.findById(codeQuestionId);
        if(codeQuestion.isPresent()) {
            codeQuestionRepository.delete(codeQuestion.get());
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .data("")
                    .message("Success!")
                    .build());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(Constant.FAIL)
                            .message("Không thể tìm thấy Question")
                            .build());
        }

    }

    public ResponseEntity<ResponseObject> getAllResultCodeByTest(Long testId, Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        Optional<TestEntity> testEntity = testRepository.findById(testId);
        if(testEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(Constant.FAIL)
                            .message("Không thể tìm thấy bài test")
                            .build());
        }else {
            List<CodeTestResultResponseDTO> testRecordEntities = recordRepository.findAll()
                    .stream().filter(testRecordEntity -> testRecordEntity.getTestEntity().equals(testEntity.get()))
                    .map(this::convertRecordToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .data(testRecordEntities)
                    .message("Success!")
                    .build());
        }
    }


    // duoi day la cac ham convert cho ham tren
    public CodeTestResultResponseDTO convertRecordToDTO(TestRecordEntity testRecordEntity) {
        return CodeTestResultResponseDTO.builder()
                .id(testRecordEntity.getId())
                .isDone(testRecordEntity.getIsDone())
                .startTime(testRecordEntity.getStartTime())
                .score(testRecordEntity.getScore())
                .record(testRecordEntity.getRecord())
                .user(getUserFromTestEntity(testRecordEntity))
                .cvDTO(getCVFromTestRecordEntity(testRecordEntity))
                .build();
    }

    public UserAccountCustomResponseDTO getUserFromTestEntity(TestRecordEntity testRecordEntity) {
        return UserAccountCustomResponseDTO.builder()
                .username(testRecordEntity.getUserAccountEntity().getUsernameReal())
                .avatar(testRecordEntity.getUserAccountEntity().getUserInfo().getAvatar())
                .email(testRecordEntity.getUserAccountEntity().getEmail())
                .fullName(testRecordEntity.getUserAccountEntity().getUserInfo().getFullName())
                .status(testRecordEntity.getUserAccountEntity().getStatus().toString())
                .build();
    }

    public CvDTO getCVFromTestRecordEntity(TestRecordEntity testRecordEntity) {
        Optional<CVEntity> cvEntity = cvRepository.findByUserAccountEntityAndJobPostingEntity(testRecordEntity.getUserAccountEntity(), testRecordEntity.getTestEntity().getJobPostingEntity());
        if(cvEntity.isPresent()) {
            return CvDTO.builder()
                    .id(cvEntity.get().getId())
                    .idJobPosting(cvEntity.get().getJobPostingEntity().getId())
                    .url(cvEntity.get().getUrl())
                    .jobPostingEntity(null)
                    .state(cvEntity.get().getState())
                    .view(cvEntity.get().isView())
                    .labels(cvEntity.get().getLabels())
                    .userAccountEntity(null)
                    .build();
        } else {
            return CvDTO.builder()
                    .build();
        }

    }

}
