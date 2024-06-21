package com.java08.quanlituyendung.entity.GoogleToken;


import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class GoogleToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    private UserAccountEntity userAccount;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.expirationTime = LocalDateTime.now().plusHours(12);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }
}