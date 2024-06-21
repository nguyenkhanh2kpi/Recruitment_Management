package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.entity.GoogleToken.GoogleToken;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.service.impl.GoogleTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tokens")
public class GoogleTokenController {

    @Autowired
    private GoogleTokenService googleTokenService;

    @Autowired
    private UserAccountRetriever userAccountRetriever;

    @PostMapping
    public GoogleToken saveOrUpdateToken(@RequestBody String token, Authentication authentication) {
        UserAccountEntity userAccount = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        return googleTokenService.saveOrUpdateToken(userAccount, token);
    }

    @GetMapping
    public Optional<GoogleToken> getToken(Authentication authentication) {
        UserAccountEntity userAccount = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        return googleTokenService.getToken(userAccount);
    }
}