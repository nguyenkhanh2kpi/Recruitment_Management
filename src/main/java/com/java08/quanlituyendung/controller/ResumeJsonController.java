package com.java08.quanlituyendung.controller;


import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.Resume.ResumeJsonDTO;
import com.java08.quanlituyendung.dto.Resume.UpdateResumeDTO;
import com.java08.quanlituyendung.service.IResumeJsonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("resume-json")
@RequiredArgsConstructor
@Tag(name = "ResumeJson")
public class ResumeJsonController {

    private final IResumeJsonService resumeJsonService;

    @Operation(summary = "Get hoặc tạo resumeJson")
    @GetMapping("")
    public ResponseEntity<ResponseObject> getOrCreateResume(Authentication authentication) {
        return resumeJsonService.getMyResume(authentication);
    }

    @Operation(summary = "update")
    @PutMapping("")
    public ResponseEntity<ResponseObject> update(@RequestBody ResumeJsonDTO request, Authentication authentication) {
        return resumeJsonService.updateResume(request, authentication);
    }
}
