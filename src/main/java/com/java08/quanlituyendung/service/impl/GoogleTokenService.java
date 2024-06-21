package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.entity.GoogleToken.GoogleToken;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.GoogleTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleTokenService {
    @Autowired
    private GoogleTokenRepository googleTokenRepository;

    public GoogleToken saveOrUpdateToken(UserAccountEntity userAccount, String token) {
        Optional<GoogleToken> existingToken = googleTokenRepository.findByUserAccount(userAccount);
        GoogleToken googleToken;

        if (existingToken.isPresent()) {
            googleToken = existingToken.get();
            googleToken.setToken(token);
        } else {
            googleToken = new GoogleToken();
            googleToken.setToken(token);
            googleToken.setUserAccount(userAccount);
        }

        return googleTokenRepository.save(googleToken);
    }

    public Optional<GoogleToken> getToken(UserAccountEntity userAccount) {
        return googleTokenRepository.findByUserAccount(userAccount);
    }
}