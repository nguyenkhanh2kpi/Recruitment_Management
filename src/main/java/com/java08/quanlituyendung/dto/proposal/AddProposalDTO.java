package com.java08.quanlituyendung.dto.proposal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProposalDTO {
    private String message;
    private Long cvId;
}
