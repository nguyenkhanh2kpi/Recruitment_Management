package com.java08.quanlituyendung.service.impl;


import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.converter.CvConverter;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.dto.proposal.AddProposalDTO;
import com.java08.quanlituyendung.dto.proposal.ProposalResponseDTO;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.ProposalEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.CvRepository;
import com.java08.quanlituyendung.repository.ProposalRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProposalServiceImpl {

    @Autowired
    ProposalRepository proposalRepository;
    @Autowired
    UserAccountRetriever userAccountRetriever;
    @Autowired
    CvRepository cvRepository;
    @Autowired
    CvConverter converter;

    public ResponseEntity<ResponseObject> getMyProposal(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        String email = user.getEmail();
        List<ProposalEntity> proposalEntities = proposalRepository.findAll().stream().filter(proposalEntity ->
                proposalEntity.getToEmail().equals(email)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.toString())
                        .message("SUCCESS")
                        .data(proposalEntities.stream().map(this::toDTO).toList())
                        .build());
    }

    public ResponseEntity<ResponseObject> getAllMyProposal(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        String email = user.getEmail();
        List<ProposalEntity> p = proposalRepository.findAll().stream().filter(proposalEntity ->
                proposalEntity.getFromEmail().equals(email)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.toString())
                        .message("SUCCESS")
                        .data(p)
                        .build());
    }

    public ResponseEntity<ResponseObject> addNewProposal(AddProposalDTO request, Authentication authentication) {
        Long cvId = request.getCvId();
        if (cvId == null) {
            return ResponseEntity.ok().body(ResponseObject.builder().message("CV ID cannot be null").build());
        }

        Optional<CVEntity> cvEntityOptional = cvRepository.findById(cvId);
        if (cvEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CVEntity cvEntity = cvEntityOptional.get();
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);

        ProposalEntity p = ProposalEntity.builder()
                .fromEmail(user.getEmail())
                .toEmail(cvEntity.getUserAccountEntity().getEmail())
                .cvId(cvId)
                .message(request.getMessage())
                .build();

        proposalRepository.save(p);

        if (cvEntity.getState() != CVEntity.State.ACCEPT) {
            cvEntity.setState(CVEntity.State.SEND_PROPOSAL);
            cvRepository.save(cvEntity);
        }

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("OK")
                .data(p)
                .build());
    }


    public ResponseEntity<ResponseObject> sendState(String state, Long proposalId) {
        ProposalEntity proposalEntity  = proposalRepository.findById(proposalId).orElse(null);
        CVEntity cvEntity = cvRepository.findById(proposalEntity.getCvId()).orElse(null);
        if(proposalEntity == null && cvEntity==null) {
            return ResponseEntity.ok(ResponseObject.builder().message("Error").build());
        } else {
            if(state.contains(CVEntity.State.ACCEPT.toString())) {
                cvEntity.setState(CVEntity.State.ACCEPT);
            } else if(state.contains(CVEntity.State.REJECT.toString())) {
                cvEntity.setState(CVEntity.State.REJECT);
            }
            cvRepository.save(cvEntity);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseObject.builder()
                            .status(HttpStatus.OK.toString())
                            .message("OK")
                            .data(converter.toDTO(cvEntity))
                            .build());

        }
    }

    public ProposalResponseDTO toDTO(ProposalEntity proposalEntity) {
        return ProposalResponseDTO.builder()
                .id(proposalEntity.getId())
                .cvId(proposalEntity.getCvId())
                .fromEmail(proposalEntity.getFromEmail())
                .toEmail(proposalEntity.getToEmail())
                .message(proposalEntity.getMessage())
                .jobName(getCvFromProposal(proposalEntity).getJobPostingEntity().getName())
                .status(getStatusFromCV(proposalEntity))
                .build();
    }

    public CVEntity getCvFromProposal(ProposalEntity proposalEntity) {
        CVEntity cv = cvRepository.findById(proposalEntity.getCvId()).orElse(null);
        return  cv;
    }

    public String getStatusFromCV(ProposalEntity proposalEntity) {
        CVEntity cv = cvRepository.findById(proposalEntity.getCvId()).orElse(null);
        return  cv.getState().toString();
    }
}
