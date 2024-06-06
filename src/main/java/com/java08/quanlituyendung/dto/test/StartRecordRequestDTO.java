package com.java08.quanlituyendung.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartRecordRequestDTO {
    private Long id;
    private Long testId;
    private String startTime;
}
