package com.java08.quanlituyendung.config;

import com.java08.quanlituyendung.helper.AuthHandshakeInterceptor;
import com.java08.quanlituyendung.service.impl.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@Configuration
@EnableWebSocket
public class SockerConfig implements WebSocketConfigurer {
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatService(), "/socket/chat").addInterceptors(authHandshakeInterceptor).setAllowedOrigins("*");
    }
}
