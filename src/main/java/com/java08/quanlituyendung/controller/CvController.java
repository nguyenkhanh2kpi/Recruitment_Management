package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ApplyJob.ApplyJobNewCVDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.ICvService;
import com.java08.quanlituyendung.utils.Constant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/apply-job")
@Tag(name = "CV")
public class CvController {
    @Autowired
    private ICvService cvService;
    @Operation(summary = "Sử dụng phương thức này để candidate apply cv vào job posting")
    @PostMapping
    public ResponseEntity<ResponseObject> applyJob(@RequestBody Long idJd, Authentication authentication){
        return cvService.applyJob(idJd,authentication);
    }

    @Operation(summary = "Sử dụng phương thức này để candidate apply cv vào job posting  với 1 cv mới")
    @PostMapping("/quick")
    public ResponseEntity<ResponseObject> applyJobNewCV(@RequestBody ApplyJobNewCVDTO request, Authentication authentication) throws IOException {
        return cvService.applyJobNewCV(request,authentication);
    }

    @Operation(summary = "Lấy tất cả CV")
    @GetMapping
    public ResponseEntity<ResponseObject> getAllCv() {
        return cvService.getAllCV();
    }

}
