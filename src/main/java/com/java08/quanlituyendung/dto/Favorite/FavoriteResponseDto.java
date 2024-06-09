package com.java08.quanlituyendung.dto.Favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponseDto {
    private Long id;
    private Long jobId;
    private String jobName;
    private String jobPosition;
    //

}