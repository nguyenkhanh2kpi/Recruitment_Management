package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.TestConverter;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.TestResultDTO.CodeTestResultResponseDTO;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.Status;
import com.java08.quanlituyendung.entity.Test.CodeQuestionEntity;
import com.java08.quanlituyendung.entity.Test.TestEntity;
import com.java08.quanlituyendung.entity.Test.TestRecordEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.UserInfoEntity;
import com.java08.quanlituyendung.repository.*;
import com.java08.quanlituyendung.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CodeQuestionServiceTest {

    @Mock
    private CodeQuestionRepository codeQuestionRepository;
    @Mock
    private UserAccountRetriever userAccountRetriever;
    @Mock
    private UserInfoEntity userInfoEntity;
    @Mock
    private MulQuestionRepository mulQuestionRepository;
    @Mock
    private TestRepository testRepository;
    @Mock
    private JobPostingRepository jobPostingRepository;
    @Mock
    private TestConverter testConverter;
    @Mock
    private MulOptionRepository mulOptionRepository;
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private CvRepository cvRepository;

    @InjectMocks
    private CodeQuestionService codeQuestionService;

    @Mock
    private Authentication authentication;
    @Mock
    private Status status;

    @Mock
    private TestEntity testEntity;

    @Mock
    private JobPostingEntity jobPostingEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addQuestion() {
        UserAccountEntity mockUser = new UserAccountEntity();
        AddCodeQuestion request = new AddCodeQuestion();
        request.setTestId(1L);
        request.setQuestionText("Sample question text");
        request.setValue("asd");
        request.setLanguage("Java");
        request.setTestCase("testCase");
        request.setTestFunction("testFunction");

        TestEntity mockTestEntity = new TestEntity();
        mockTestEntity.setCodeQuestions(new ArrayList<>());

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(mockUser);
        when(testRepository.findById(request.getTestId())).thenReturn(Optional.of(mockTestEntity));

        ResponseEntity<ResponseObject> response = codeQuestionService.addQuestion(authentication, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Question added successfully", response.getBody().getMessage());
        verify(testRepository, times(1)).save(mockTestEntity);
    }

    @Test
    void addQuestion_UserOrTestNotFound() {
        AddCodeQuestion request = new AddCodeQuestion();
        request.setTestId(1L);

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(null);
        when(testRepository.findById(request.getTestId())).thenReturn(Optional.empty());

        ResponseEntity<ResponseObject> response = codeQuestionService.addQuestion(authentication, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constant.FAIL, response.getBody().getStatus());
        assertEquals("Failed to add question: User not found or Test not found", response.getBody().getMessage());
    }

    @Test
    void getCodeQuestionByTestId() {
        Long testId = 1L;
        UserAccountEntity mockUser = new UserAccountEntity();
        TestEntity mockTestEntity = new TestEntity();
        mockTestEntity.setCodeQuestions(new ArrayList<>());

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(mockUser);
        when(testRepository.findById(testId)).thenReturn(Optional.of(mockTestEntity));

        ResponseEntity<ResponseObject> response = codeQuestionService.getCodeQuestionByTestId(testId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success!", response.getBody().getMessage());
        assertEquals(mockTestEntity.getCodeQuestions(), response.getBody().getData());
    }

    @Test
    void getCodeQuestionByTestId_UserOrTestNotFound() {
        Long testId = 1L;

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(null);
        when(testRepository.findById(testId)).thenReturn(Optional.empty());


        ResponseEntity<ResponseObject> response = codeQuestionService.getCodeQuestionByTestId(testId, authentication);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constant.FAIL, response.getBody().getStatus());
        assertEquals("Không thể tìm thấy testId", response.getBody().getMessage());
    }

    @Test
    void deleteCodeQuestion() {

        Long codeQuestionId = 1L;
        CodeQuestionEntity mockCodeQuestion = new CodeQuestionEntity();

        when(codeQuestionRepository.findById(codeQuestionId)).thenReturn(Optional.of(mockCodeQuestion));


        ResponseEntity<ResponseObject> response = codeQuestionService.deleteCodeQuestion(codeQuestionId, authentication);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success!", response.getBody().getMessage());
        verify(codeQuestionRepository, times(1)).delete(mockCodeQuestion);
    }

    @Test
    void deleteCodeQuestion_QuestionNotFound() {

        Long codeQuestionId = 1L;

        when(codeQuestionRepository.findById(codeQuestionId)).thenReturn(Optional.empty());


        ResponseEntity<ResponseObject> response = codeQuestionService.deleteCodeQuestion(codeQuestionId, authentication);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constant.FAIL, response.getBody().getStatus());
        assertEquals("Không thể tìm thấy Question", response.getBody().getMessage());
    }

    @Test
    void getAllResultCodeByTest() {

        Long testId = 1L;
        UserAccountEntity mockUser = new UserAccountEntity();
        TestEntity mockTestEntity = new TestEntity();
        List<TestRecordEntity> testRecordEntities = new ArrayList<>();

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(mockUser);
        when(testRepository.findById(testId)).thenReturn(Optional.of(mockTestEntity));
        when(recordRepository.findAll()).thenReturn(testRecordEntities);


        ResponseEntity<ResponseObject> response = codeQuestionService.getAllResultCodeByTest(testId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success!", response.getBody().getMessage());
        verify(recordRepository, times(1)).findAll();
    }

    @Test
    void getAllResultCodeByTest_TestNotFound() {
        Long testId = 1L;

        when(userAccountRetriever.getUserAccountEntityFromAuthentication(authentication)).thenReturn(null);
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        ResponseEntity<ResponseObject> response = codeQuestionService.getAllResultCodeByTest(testId, authentication);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constant.FAIL, response.getBody().getStatus());
        assertEquals("Không thể tìm thấy bài test", response.getBody().getMessage());
    }

    @Test
    void convertRecordToDTO() {
        UserAccountEntity mockUser = new UserAccountEntity();
        UserInfoEntity mockUserInfo = new UserInfoEntity();
        TestEntity mockTest = new TestEntity();
        JobPostingEntity mockJobPostingEntity = new JobPostingEntity();
        mockTest.setJobPostingEntity(mockJobPostingEntity);
        mockUser.setUserInfo(mockUserInfo);
        mockUser.setStatus(status);
        TestRecordEntity mockTestRecordEntity = new TestRecordEntity();
        mockTestRecordEntity.setTestEntity(mockTest);
        mockTestRecordEntity.setId(1L);
        mockTestRecordEntity.setIsDone(true);
        mockTestRecordEntity.setStartTime("123456789L");
        mockTestRecordEntity.setScore(9.0);
        mockTestRecordEntity.setRecord("Sample record");
        mockTestRecordEntity.setUserAccountEntity(mockUser);

        CodeTestResultResponseDTO resultDTO = codeQuestionService.convertRecordToDTO(mockTestRecordEntity);

        assertEquals(mockTestRecordEntity.getId(), resultDTO.getId());
        assertEquals(mockTestRecordEntity.getIsDone(), resultDTO.getIsDone());
        assertEquals(mockTestRecordEntity.getStartTime(), resultDTO.getStartTime());
        assertEquals(mockTestRecordEntity.getScore(), resultDTO.getScore());
        assertEquals(mockTestRecordEntity.getRecord(), resultDTO.getRecord());
    }
}
