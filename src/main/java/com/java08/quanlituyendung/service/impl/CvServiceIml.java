package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.TestConverter;
import com.java08.quanlituyendung.dto.ApplyJob.ApplyJobNewCVDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.UserCV_AppliedJob.AppliedJobDTO;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.Test.TestEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.notify.NotifyEntity;
import com.java08.quanlituyendung.repository.*;
import com.java08.quanlituyendung.service.ICvService;
import com.java08.quanlituyendung.service.IFileUploadService;
import com.java08.quanlituyendung.service.INotifyService;
import com.java08.quanlituyendung.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    INotifyService notifyService;
    @Autowired
    NotifyRepository notifyRepository;
    @Autowired
    TestRepository testRepository;
    @Autowired
    TestConverter testConverter;


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

                            notifyService.sendNotification("Kiểm tra sàng lọc",
                                    "Job: "+ jobPostingEntity.getName()
                                            + " có yêu cầu thực hiện bài test sàng lọc ",
                                    userAccountEntity.getEmail(),
                                    "/test"
                            );

                            return ResponseEntity.ok(ResponseObject.builder().status(HttpStatus.OK.toString())
                                    .message("Update CV success!")
                                    .build());
                        }
                    }

                    notifyService.sendNotification("Kiểm tra sàng lọc",
                            "Job: "+ jobPostingEntity.getName()
                                    + " có yêu cầu thực hiện bài test sàng lọc ",
                            userAccountEntity.getEmail(),
                            "/test"
                    );

                    CVEntity cvEntity = new CVEntity();
                    cvEntity.setState(CVEntity.State.RECEIVE_CV);
                    cvEntity.setView(false);
                    cvEntity.setUserAccountEntity(userAccountEntity);
                    cvEntity.setJobPostingEntity(jobPostingEntity);
                    cvEntity.setUrl(urlCv);
                    cvEntity.setLabels("{}");
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

                notifyService.sendNotification("Kiểm tra sàng lọc",
                        "Job: "+ jobPostingEntity.getName()
                                + " có yêu cầu thực hiện bài test sàng lọc ",
                        user.getEmail(),
                        "/test"
                );

                cvEntityCheck.setUrl(request.getCv());
                cvEntityCheck.setDateApply(LocalDate.now().toString());
                cvRepository.save(cvEntityCheck);
                return ResponseEntity.ok(ResponseObject.builder().status(HttpStatus.OK.toString())
                        .message("Update CV success!")
                        .build());
            }
        }
        notifyService.sendNotification("Kiểm tra sàng lọc",
                "Job: "+ jobPostingEntity.getName()
                        + " có yêu cầu thực hiện bài test sàng lọc ",
                user.getEmail(),
                "/test"
        );
        CVEntity cvEntity = new CVEntity();
        cvEntity.setState(CVEntity.State.RECEIVE_CV);
        cvEntity.setView(false);
        cvEntity.setLabels("");
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

    @Override
    public ResponseEntity<ResponseObject> updateStatus(Long id, String status) {
        Optional<CVEntity> optionalCvEntity = cvRepository.findById(id);

        if (!optionalCvEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("CV not found")
                            .build()
            );
        }
        notifyService.sendNotification("Nhà tuyển dụng cập nhật CV",
                "Job: "+ optionalCvEntity.get().getJobPostingEntity().getName()
                + " bạn đã được nhà tuyển dụng cập nhật trạng thái CV thành: " + status,
                "johndoe@gmail.com",
                "/jobDetail/"+optionalCvEntity.get().getJobPostingEntity().getId()
                );
        CVEntity cvEntity = optionalCvEntity.get();
        cvEntity.setState(getState(status));
        cvRepository.save(cvEntity);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Update status success!")
                .build());
    }

    public CVEntity.State getState(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }

        for (CVEntity.State enumState : CVEntity.State.values()) {
            if (enumState.name().equalsIgnoreCase(state.trim())) {
                return enumState;
            }
        }

        throw new IllegalArgumentException("Invalid state value: " + state);
    }

    @Override
    public ResponseEntity<ResponseObject> updateLabel(Long id, String label) {
        Optional<CVEntity> optionalCvEntity = cvRepository.findById(id);
        if (!optionalCvEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("CV not found")
                            .build()
            );
        }

        CVEntity cvEntity = optionalCvEntity.get();
        cvEntity.setLabels(label);
        cvRepository.save(cvEntity);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Update label success!")
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> getCVById(Long id) {
        List<CVEntity> CVS = cvRepository.findAll().stream().filter(cvEntity -> cvEntity.getId()==id).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message(Constant.SUCCESS)
                .data(CVS)
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> updateView(Long id, Boolean status) {
        Optional<CVEntity> optionalCvEntity = cvRepository.findById(id);
        if (!optionalCvEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("CV not found")
                            .build()
            );
        }

        CVEntity cvEntity = optionalCvEntity.get();
        cvEntity.setView(status);
        cvRepository.save(cvEntity);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Update view success!")
                .build());
    }

    @Override
    public ResponseEntity<ResponseObject> getAllMyAppliedJob(Authentication authentication) {
        try {
            UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
            List<CVEntity> cvEntities = cvRepository.findAll().stream()
                    .filter(cvEntity -> cvEntity.getUserAccountEntity().equals(user)).collect(Collectors.toList());
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .message("Success!")
                    .data(cvEntities.stream().map(this::CVToAppliedJobDTO).collect(Collectors.toList()))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .message("Internal error")
                            .build());
        }
    }

    public AppliedJobDTO CVToAppliedJobDTO(CVEntity cv) {
        List<NotifyEntity> notifyEntities = notifyRepository.findAll()
                .stream().filter(notifyEntity ->
                     notifyEntity.getMessage().contains(cv.getJobPostingEntity().getName())
                             && notifyEntity.getRecipient().contains(cv.getUserAccountEntity().getEmail())
                ).collect(Collectors.toList());
        List<TestEntity> testEntities = testRepository.findAll()
                .stream().filter(test -> test.getJobPostingEntity().equals(cv.getJobPostingEntity()))
                .collect(Collectors.toList());
        return AppliedJobDTO.builder()
                .CVid(cv.getId())
                .CVurl(cv.getUrl())
                .dateApply(cv.getDateApply())
                .state(cv.getState().toString())
                .view(cv.isView())
                .jobPostingId(cv.getJobPostingEntity().getId())
                .relatedNotify(notifyEntities)
                .testList(testEntities.stream()
                        .map(testEntity -> testConverter.toDTO_AppliedPage(testEntity, cv.getUserAccountEntity()))
                        .collect(Collectors.toList()))
                .build();
    }


}
