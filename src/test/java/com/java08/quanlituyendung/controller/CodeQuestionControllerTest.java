package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.service.impl.CodeQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CodeQuestionControllerTest {

    @Mock
    private CodeQuestionService codeQuestionService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CodeQuestionController codeQuestionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addQuestion() {
        AddCodeQuestion question = new AddCodeQuestion();
        ResponseObject responseObject = new ResponseObject("success", "Question added", null);
        when(codeQuestionService.addQuestion(any(Authentication.class), any(AddCodeQuestion.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(responseObject));

        ResponseEntity<ResponseObject> response = codeQuestionController.addQuestion(authentication, question);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseObject, response.getBody());
    }

    @Test
    void getCodeQuestionByTestId() {
        Long testId = 1L;
        ResponseObject responseObject = new ResponseObject("success", "Questions retrieved", null);
        when(codeQuestionService.getCodeQuestionByTestId(eq(testId), any(Authentication.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(responseObject));

        ResponseEntity<ResponseObject> response = codeQuestionController.getCodeQuestionByTestId(testId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseObject, response.getBody());
    }

    @Test
    void deleteCodeQuestion() {
        Long codeQuestionId = 1L;
        ResponseObject responseObject = new ResponseObject("success", "Question deleted", null);
        when(codeQuestionService.deleteCodeQuestion(eq(codeQuestionId), any(Authentication.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(responseObject));

        ResponseEntity<ResponseObject> response = codeQuestionController.deleteCodeQuestion(codeQuestionId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseObject, response.getBody());
    }

    @Test
    void getAllResultCodeByTest() {
        Long testId = 1L;
        ResponseObject responseObject = new ResponseObject("success", "Results retrieved", null);
        when(codeQuestionService.getAllResultCodeByTest(eq(testId), any(Authentication.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(responseObject));

        ResponseEntity<ResponseObject> response = codeQuestionController.getAllResultCodeByTest(testId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseObject, response.getBody());
    }
}
