package com.java08.quanlituyendung.entity.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ResumeJsonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userAccountId")
    private UserAccountEntity userAccountEntity;
    private String fullName;
    private String applicationPosition;
    @Column(name = "jsonResume", columnDefinition = "TEXT")
    private String jsonResume;
}
