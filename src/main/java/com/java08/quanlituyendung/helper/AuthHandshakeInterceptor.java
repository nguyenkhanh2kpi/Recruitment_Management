package com.java08.quanlituyendung.helper;


import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final UserAccountRetriever userAccountRetriever = new UserAccountRetriever();
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String generateSessionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
