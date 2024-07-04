package com.java08.quanlituyendung.dto.InterviewPayload;

import com.java08.quanlituyendung.dto.JobPostingDTO;
import com.java08.quanlituyendung.entity.CVEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewDetailResponseDTO {
    private Long id;
    private Long roomId;
    private String roomName;
    private String interviewTime;
    private CandidateRoomItemDTO candidate;
    private CVEntity cv;
    private String description;
    private Float averageScores;
    private String comment;
    private String englishQuestion;
    private String technicalQuestion;
    private String softSkillQuestion;
    private String interviewer;
    private JobPostingDTO jobPosting;


}
