package com.java08.quanlituyendung.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JobPosting")
public class JobPostingEntity extends BaseEntity{
    private String name;
    private String position;
    private String language;
    private String location;
    private String salary;
    private String number;
    private String workingForm;
    private String sex;
    private String experience;
    private String detailLocation;
    @Column(name = "detailJob", columnDefinition = "text")
    private String detailJob;
    @Column(name = "requirements", columnDefinition = "text")
    private String requirements;
    @Column(name = "interest", columnDefinition = "text")
    private String interest;
    @Column(name = "image", columnDefinition = "text")
    private String image;
    private String createDate;
    private String updateDate;
    private String expriredDate;
    private Boolean status;

    // danh sach ung vien
    @OneToMany(mappedBy = "jobPostingEntity")
    private List<CVEntity> cvEntities;

    // danh sach nguoi phong van ?
    @OneToMany(mappedBy = "jobPostingEntity")
    private List<InterviewEntity> interviewEntity;

    @ManyToOne
    @JoinColumn(name = "userAccountId")
    private UserAccountEntity userAccountEntity;

    private Boolean requireTest;

    @Enumerated(EnumType.STRING)
    private JobPostingEntity.State state;
    public enum State {
        CREATE,
        ON,
        PAUSE,
        END
    }
    private String industry;
    private String industry2;
    private Boolean isVip;

    @Override
    public int hashCode() {
        // Implement hashCode excluding cyclic references
        return Objects.hash(getId(), getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobPostingEntity that = (JobPostingEntity) o;
        return Objects.equals(getId(), that.getId());
    }


}