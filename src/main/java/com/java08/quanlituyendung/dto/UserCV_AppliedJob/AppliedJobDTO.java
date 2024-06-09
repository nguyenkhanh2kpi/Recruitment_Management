package com.java08.quanlituyendung.dto.UserCV_AppliedJob;


import com.java08.quanlituyendung.dto.test.TestResponseDTO;
import com.java08.quanlituyendung.entity.notify.NotifyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedJobDTO {
    private Long CVid;
    private String CVurl;
    private String dateApply;
    private String state;
    private boolean view;
    private Long jobPostingId;
    private List<NotifyEntity> relatedNotify;
    private List<AppliedJobTestsDTO> testList;
}
