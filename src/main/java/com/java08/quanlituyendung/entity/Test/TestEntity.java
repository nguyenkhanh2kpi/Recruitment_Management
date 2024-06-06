package com.java08.quanlituyendung.entity.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_entity")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public enum Type{
        MULTIPLE_CHOICE,
        ESSAY,
        CODE
    }

    @OneToOne
    private UserAccountEntity userAccountEntity;

    @JsonIgnore
    @ManyToOne
    private JobPostingEntity jobPostingEntity;
    private String summary;
    private Integer time;
    private Boolean isDelete;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "essayQuestion", columnDefinition = "TEXT")
    private String essayQuestion;

    @JsonIgnore
    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestRecordEntity> records = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private List<MulQuestionEntity> mulQuestions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private List<CodeQuestionEntity> codeQuestions = new ArrayList<>();

}
