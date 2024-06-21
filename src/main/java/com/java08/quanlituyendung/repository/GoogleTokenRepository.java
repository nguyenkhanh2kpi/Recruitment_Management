package com.java08.quanlituyendung.repository;

import com.java08.quanlituyendung.entity.GoogleToken.GoogleToken;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Long> {
    Optional<GoogleToken> findByUserAccount(UserAccountEntity userAccount);
}