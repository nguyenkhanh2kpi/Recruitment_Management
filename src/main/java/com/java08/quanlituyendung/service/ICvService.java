package com.java08.quanlituyendung.service;

import com.java08.quanlituyendung.dto.ApplyJob.ApplyJobNewCVDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICvService {
    ResponseEntity<ResponseObject> applyJob(long idJobPosting, Authentication authentication);

    ResponseEntity<ResponseObject> getAllCV();

    ResponseEntity<ResponseObject> applyJobNewCV(ApplyJobNewCVDTO request, Authentication authentication) throws IOException;
}
