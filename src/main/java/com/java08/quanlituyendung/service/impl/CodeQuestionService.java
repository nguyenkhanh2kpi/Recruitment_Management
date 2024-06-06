package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.TestConverter;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.dto.test.AddQuestionDTO;
import com.java08.quanlituyendung.entity.Test.CodeQuestionEntity;
import com.java08.quanlituyendung.entity.Test.MulQuestionEntity;
import com.java08.quanlituyendung.entity.Test.MulQuestionOptionEntity;
import com.java08.quanlituyendung.entity.Test.TestEntity;
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
import java.util.Optional;

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
                    .value("")
                    .language("")
                    .testCase("")
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
}
