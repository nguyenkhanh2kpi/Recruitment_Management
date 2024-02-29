package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    UserAccountRetriever userAccountRetriever;

    MessageServiceImpl(UserAccountRetriever userAccountRetriever) {
        this.userAccountRetriever = userAccountRetriever;
    }
    @Override
    public ResponseEntity<ResponseObject> getAllMyMessage(Authentication authentication) {
        UserAccountEntity userAccountEntity = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);

        return null;
    }
}
