package com.java08.quanlituyendung.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCodeQuestion {
    private Long jdId;
    private Long testId;
    private String questionText;
    private String value;
    private String language;
    private String testCase;
}
