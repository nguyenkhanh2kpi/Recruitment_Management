package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.dto.test.AddQuestionDTO;
import com.java08.quanlituyendung.service.impl.CodeQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mul-test-code")
@Tag(name = "Kiểm tra code")
public class CodeQuestionController {

    @Autowired
    private CodeQuestionService codeQuestionService;
    @Operation(summary = "thêm câu hỏi")
    @PostMapping("/add-question")
    public ResponseEntity<ResponseObject> addQuestion(Authentication authentication, @RequestBody AddCodeQuestion questionText) {
        return codeQuestionService.addQuestion(authentication, questionText);
    }
}