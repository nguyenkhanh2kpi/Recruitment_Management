package com.java08.quanlituyendung.entity.Test;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mul_question")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CodeQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "questionText", columnDefinition = "TEXT")
    private String questionText;
    private String value;
    private String language;

    @Column(name = "testCase", columnDefinition = "TEXT")
    private String testCase;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestEntity test;


}
