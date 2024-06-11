package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.ResumeConverter;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.Resume.ResumeJsonDTO;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.sample.ResumeJsonEntity;
import com.java08.quanlituyendung.repository.ResumeJsonRepository;
import com.java08.quanlituyendung.service.IResumeJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ResumeJsonServiceImpl implements IResumeJsonService {

    @Autowired
    private ResumeJsonRepository resumeRepository;

    @Autowired
    private UserAccountRetriever userAccountRetriever;

    @Autowired
    private ResumeConverter resumeConverter;

    @Override
    public ResponseEntity<ResponseObject> getMyResume(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user == null) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.NOT_ACCEPTABLE.toString())
                    .message("Something went wrong")
                    .build());
        }
        var resume = resumeRepository.findByUserAccountEntity(user);
        if (resume.isEmpty()) {
            ResumeJsonEntity resumeEntity = resumeConverter.newResumeEntity(user);
            resumeRepository.save(resumeEntity);
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.CREATED.toString())
                    .message("Success")
                    .data(resumeConverter.toDto(resumeEntity))
                    .build());
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Success")
                .data(resumeConverter.toDto(resume.get()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> updateResume(ResumeJsonDTO request, Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseObject.builder()
                            .status(HttpStatus.UNAUTHORIZED.toString())
                            .message("Unauthorized")
                            .build());
        }
        var resumeOpt = resumeRepository.findByUserAccountEntity(user);
        if (resumeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("Resume not found")
                            .build());
        }
        ResumeJsonEntity resumeEntity = resumeOpt.get();
        resumeEntity.setFullName(request.getFullName());
        resumeEntity.setApplicationPosition(request.getApplicationPosition());
        resumeEntity.setJsonResume(request.getResumeJson());
        resumeRepository.save(resumeEntity);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Resume updated successfully")
                .data(resumeConverter.toDto(resumeEntity))
                .build());
    }
}