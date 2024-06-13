package com.java08.quanlituyendung.dto.TestResultDTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java08.quanlituyendung.dto.CvDTO;
import com.java08.quanlituyendung.dto.UserAccountPayload.UserAccountCustomResponseDTO;
import com.java08.quanlituyendung.entity.Test.TestEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTestResultResponseDTO {
    private Long id;
    private UserAccountCustomResponseDTO user;
    private CvDTO cvDTO;
    private Boolean isDone;
    private String startTime;
    private Double score;
    private String record;

}
