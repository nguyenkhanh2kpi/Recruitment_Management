package com.java08.quanlituyendung.dto.UserCV_AppliedJob;

import com.java08.quanlituyendung.dto.test.MulQuestionResponseDTO;
import com.java08.quanlituyendung.entity.Test.CodeQuestionEntity;
import com.java08.quanlituyendung.entity.Test.TestEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedJobTestsDTO {
    private Long id;
    private Long jdId;
    private Integer time;
    private String summary;
    private String job;
    private boolean isRecord;
    private boolean isStart;
    private String startTime;
    private TestEntity.Type type;
}
