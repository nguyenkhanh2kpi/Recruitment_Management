package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.DashBoard.ReccerDashDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.InterviewEntity;
import com.java08.quanlituyendung.repository.CvRepository;
import com.java08.quanlituyendung.repository.InterviewRepository;
import com.java08.quanlituyendung.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashService {
    @Autowired
    private CvRepository cvRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private UserAccountRetriever userAccountRetriever;
    @Autowired
    private InterviewRepository interviewRepository;

    public ResponseEntity<ResponseObject> getReccer(Authentication authentication) {
        var user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        List<CVEntity> cvs = cvRepository.findAll().stream().filter(cvEntity -> cvEntity.getJobPostingEntity().getUserAccountEntity().equals(user)).toList();
        List<InterviewEntity> interviewEntities = interviewRepository.findAll().stream().filter(interviewEntity -> interviewEntity.getJobPostingEntity().getUserAccountEntity().equals(user)).toList();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<Integer, Long> cvRcByMonth = cvs.stream()
                .collect(Collectors.groupingBy(
                        cvEntity -> LocalDate.parse(cvEntity.getDateApply(), formatter).getMonthValue(),
                        Collectors.counting()
                ));

        Map<Integer, Long> cvAcceptByMonth = cvs.stream()
                .filter(cvEntity -> cvEntity.getState().equals(CVEntity.State.ACCEPT))
                .collect(Collectors.groupingBy(
                        cvEntity -> LocalDate.parse(cvEntity.getDateApply(), formatter).getMonthValue(),
                        Collectors.counting()
                ));


        ReccerDashDTO data = ReccerDashDTO.builder()
                .openPost(jobPostingRepository.findAll().stream().filter(j -> j.getUserAccountEntity().equals(user)).count())
                .receivedCV(cvs.stream().filter(cvEntity -> cvEntity.getState().equals(CVEntity.State.RECEIVE_CV)).count())
                .cvToday(cvs.stream().filter(cvEntity -> !cvEntity.isView()).count())
                .numInterview(interviewEntities.stream().count())
                .openInterview(interviewEntities.stream().filter(interviewEntity -> interviewEntity.getStatus().equals("Processing")).count())
                .closeInterview(interviewEntities.stream().filter(interviewEntity -> interviewEntity.getStatus().equals("Ended")).count())
                .cvRc(cvRcByMonth)
                .cvAccept(cvAcceptByMonth)
                .build();

        return ResponseEntity.ok(ResponseObject.builder()
                        .data(data)
                .build());
    }
}
