package com.java08.quanlituyendung.service;

import com.java08.quanlituyendung.dto.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface ICvService {
    ResponseEntity<ResponseObject> applyJob(long idJobPosting, Authentication authentication);

    ResponseEntity<ResponseObject> getAllCV();
}
