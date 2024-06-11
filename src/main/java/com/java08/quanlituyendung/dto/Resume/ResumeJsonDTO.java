package com.java08.quanlituyendung.dto.Resume;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeJsonDTO {
    private Long id;
    private String fullName;
    private String applicationPosition;
    private String resumeJson;
}
