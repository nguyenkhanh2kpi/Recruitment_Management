package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.test.AddCodeQuestion;
import com.java08.quanlituyendung.dto.test.AddQuestionDTO;
import com.java08.quanlituyendung.entity.Test.TestRecordEntity;
import com.java08.quanlituyendung.repository.RecordRepository;
import com.java08.quanlituyendung.service.impl.CodeQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "lấy danh sách câu hỏi code theo id bài test")
    @GetMapping("/{testId}")
    public ResponseEntity<ResponseObject> getCodeQuestionByTestId(@PathVariable Long testId, Authentication authentication) {
        return codeQuestionService.getCodeQuestionByTestId(testId, authentication);
    }
    @Operation(summary = "Xóa câu hỏi khỏi code test")
    @DeleteMapping("/{codeQuestionId}")
    public ResponseEntity<ResponseObject> deleteCodeQuestion(@PathVariable Long codeQuestionId, Authentication authentication) {
        return codeQuestionService.deleteCodeQuestion(codeQuestionId, authentication);
    }


    @Operation(summary = "Lấy tâts cả bản record theo test id")
    @GetMapping("/result/{testId}")
    public ResponseEntity<ResponseObject> getAllResultCodeByTest(@PathVariable Long testId, Authentication authentication) {
        return  codeQuestionService.getAllResultCodeByTest(testId, authentication);
    }



}
