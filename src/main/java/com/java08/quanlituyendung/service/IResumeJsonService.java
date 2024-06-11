package com.java08.quanlituyendung.service;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.Resume.ResumeJsonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface IResumeJsonService {
    ResponseEntity<ResponseObject> getMyResume(Authentication authentication);

    ResponseEntity<ResponseObject> updateResume(ResumeJsonDTO request, Authentication authentication);
}
