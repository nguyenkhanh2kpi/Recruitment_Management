package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.ApplyJob.ApplyJobNewCVDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.CvRepository;
import com.java08.quanlituyendung.repository.JobPostingRepository;
import com.java08.quanlituyendung.repository.UserInfoRepository;
import com.java08.quanlituyendung.service.ICvService;
import com.java08.quanlituyendung.service.IFileUploadService;
import com.java08.quanlituyendung.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class CvServiceIml implements ICvService {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserAccountRetriever userAccountRetriever;
    @Autowired
    private CvRepository cvRepository;
    @Autowired
    IFileUploadService iFileUploadService;

    public ResponseEntity<ResponseObject> applyJob(long idJobPosting, Authentication authentication) {
        if (userAccountRetriever.getUserAccountEntityFromAuthentication(authentication) != null) {
            JobPostingEntity jobPostingEntity = jobPostingRepository.findOneById(idJobPosting);
            if (jobPostingEntity != null) {
                List<CVEntity> listCvEntity = cvRepository.findAllByJobPostingEntityId(idJobPosting);
                UserAccountEntity userAccountEntity = userAccountRetriever
                        .getUserAccountEntityFromAuthentication(authentication);
                String urlCv;
                if (userInfoRepository.findOneById(userAccountEntity.getId()).getCv_pdf() != null) {
                    urlCv = userInfoRepository.findOneById(userAccountEntity.getId()).getCv_pdf();
                    for (CVEntity cvEntityCheck : listCvEntity) {
                        if (cvEntityCheck.getUserAccountEntity().getId().equals(userAccountEntity.getId())) {
                            cvEntityCheck.setUrl(urlCv);
                            cvEntityCheck.setDateApply(LocalDate.now().toString());
                            cvRepository.save(cvEntityCheck);
                            return ResponseEntity.ok(ResponseObject.builder().status(HttpStatus.OK.toString())
                                    .message("Update CV success!")
                                    .build());
                        }
                    }

                    CVEntity cvEntity = new CVEntity();
                    cvEntity.setUserAccountEntity(userAccountEntity);
                    cvEntity.setJobPostingEntity(jobPostingEntity);
                    cvEntity.setUrl(urlCv);
                    cvEntity.setDateApply(LocalDate.now().toString());
                    cvRepository.save(cvEntity);

                    return ResponseEntity.ok(ResponseObject.builder()
                            .status(HttpStatus.CREATED.toString())
                            .message("Apply CV success!")
                            .build());
                } else {
                    return ResponseEntity.ok(ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("CV doesn't exist")
                            .build());
                }

            } else {
                return ResponseEntity.ok(ResponseObject.builder()
                        .status(HttpStatus.NOT_FOUND.toString())
                        .message("Job Posting don't exist")
                        .build());
            }

        }
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message(Constant.NOT_AUTHENTICATED)
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> getAllCV() {
        var CVS = cvRepository.findAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message(Constant.SUCCESS)
                .data(CVS)
                .build());
    }
// bugg
    @Override
    public ResponseEntity<ResponseObject> applyJobNewCV(ApplyJobNewCVDTO request, Authentication authentication) throws IOException {
        if(request.getCv()=="" || request.getCv()==null) {
            return ResponseEntity.ok(ResponseObject.builder().status(HttpStatus.FOUND.toString())
                    .message("Your cv is null")
                    .build());
        }
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        JobPostingEntity jobPostingEntity = jobPostingRepository.findOneById(request.getJobId());
        List<CVEntity> listCvEntity = cvRepository.findAllByJobPostingEntityId(request.getJobId());
        for (CVEntity cvEntityCheck : listCvEntity) {
            if (cvEntityCheck.getUserAccountEntity().getId().equals(user.getId())) {
                cvEntityCheck.setUrl(request.getCv());
                cvEntityCheck.setDateApply(LocalDate.now().toString());
                cvRepository.save(cvEntityCheck);
                return ResponseEntity.ok(ResponseObject.builder().status(HttpStatus.OK.toString())
                        .message("Update CV success!")
                        .build());
            }
        }
        CVEntity cvEntity = new CVEntity();
        cvEntity.setUserAccountEntity(user);
        cvEntity.setJobPostingEntity(jobPostingEntity);
        cvEntity.setUrl(request.getCv());
        cvEntity.setDateApply(LocalDate.now().toString());
        cvRepository.save(cvEntity);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED.toString())
                .message("Apply CV success!")
                .build());
    }
}
