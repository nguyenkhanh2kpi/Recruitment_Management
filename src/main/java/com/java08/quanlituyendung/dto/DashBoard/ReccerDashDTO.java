package com.java08.quanlituyendung.dto.DashBoard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReccerDashDTO {
    private Long openPost;
    private Long receivedCV;
    private Long cvToday;
    private Long numInterview;
    private Long openInterview;
    private Long closeInterview;
    private Map<Integer, Long> cvRc;
    private Map<Integer, Long>  cvAccept;
}
