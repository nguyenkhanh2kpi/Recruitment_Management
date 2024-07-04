package com.java08.quanlituyendung.entity.FeedBack;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeedBackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String message;

    private LocalDateTime time;

    private Boolean view = false;
    private Boolean isPin = false;

    @PrePersist
    protected void onCreate() {
        time = LocalDateTime.now();
    }
}