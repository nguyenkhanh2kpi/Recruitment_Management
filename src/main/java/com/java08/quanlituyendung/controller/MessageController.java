package com.java08.quanlituyendung.controller;


import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.IMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {

    IMessageService iMessageService;

    @GetMapping("/messages")
    public ResponseEntity<ResponseObject> getMyMessage(Authentication authentication) {
        return iMessageService.getAllMyMessage(authentication);
    }
}
