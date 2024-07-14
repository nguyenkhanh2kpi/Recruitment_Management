package com.java08.quanlituyendung.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponseDTO {
    private Long id;
    private String fromEmail;
    private String toEmail;
    private Long cvId;
    private String message;
    private String status;
    private String jobName;


}
