package com.java08.quanlituyendung.entity.Test;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_record")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    private UserAccountEntity userAccountEntity;
    @ManyToOne
    @JsonIgnore
    private TestEntity testEntity;
    private Boolean isDone;
    private String startTime;
// dung cho hinh thuc trac nghiem
    private Double score;

    @Column(name = "record", columnDefinition = "TEXT")
    private String record;

}
