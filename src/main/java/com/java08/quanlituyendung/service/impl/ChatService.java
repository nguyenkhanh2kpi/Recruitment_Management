package com.java08.quanlituyendung.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.java08.quanlituyendung.helper.SessionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
@RequiredArgsConstructor
public class ChatService implements WebSocketHandler {
    private static final List<WebSocketSession> guestSessions = new ArrayList<>();
    private static final List<WebSocketSession> staffSessions = new ArrayList<>();
    @Autowired
    private SessionMessage sessionMessageManager = new SessionMessage();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Integer role = (Integer) attributes.get("role");
        if (role == 0)
            guestSessions.add(session);
        else if (role == 1 || role == 2) {
            staffSessions.add(session);
            session.sendMessage(
                    new TextMessage("Backup: " + new Gson().toJson(sessionMessageManager.getSessionMessages())));
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Gson gson = new Gson();
        String receivedMessage = (String) message.getPayload();
        Map<String, Object> attributes = session.getAttributes();
        Integer role = (Integer) attributes.get("role");
        String sessionId = (String) attributes.get("userId");
        if (role == 0) {
            sessionMessageManager.addMessage(sessionId, sessionId + ": " + receivedMessage);
            for (WebSocketSession webSocketSession : staffSessions) {
                webSocketSession.sendMessage(new TextMessage(sessionId + ": " + receivedMessage));
            }
        } else if (role == 1 || role == 2) {
            if (receivedMessage.startsWith("Close: ")) {
                String target = receivedMessage.substring(7);
                WebSocketSession targetSession = guestSessions.stream()
                        .filter(s -> s.getAttributes().get("userId").equals(target)).findFirst().get();
                Map<String, String> data = new HashMap<>();
                data.put("message", "Dạ nếu anh/chị không còn thắc mắc nào. Em xin kết thúc trò chuyện. Chúc anh/chị một ngày tốt lành ạ!");
                data.put("sender", "Assistant");
                String repData = gson.toJson(data);
                targetSession.sendMessage(new TextMessage(repData));
                targetSession.close();
            } else {
                for (WebSocketSession webSocketSession : staffSessions) {
                    if (webSocketSession != session)
                        webSocketSession.sendMessage(new TextMessage(sessionId + ": " + receivedMessage));
                }
                try {
                    JsonObject messageData = gson.fromJson(receivedMessage, JsonObject.class);
                    String guestId = messageData.get("rep").getAsString();
                    String content = messageData.get("message").getAsString();
                    String sender = messageData.get("senderInfor").getAsJsonObject().get("sender").getAsString();
                    sessionMessageManager.addMessage(guestId, sessionId + ": " + receivedMessage);
                    Map<String, String> data = new HashMap<>();
                    data.put("message", content);
                    data.put("sender", sender);
                    String repData = gson.toJson(data);
                    WebSocketSession targetSession = guestSessions.stream()
                            .filter(s -> s.getAttributes().get("userId").equals(guestId)).findFirst().get();
                    targetSession.sendMessage(new TextMessage(repData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error: " + exception.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Integer role = (Integer) attributes.get("role");
        String sessionId = (String) attributes.get("userId");
        if (role == 0) {
            sessionMessageManager.removeSession(sessionId);
            guestSessions.remove(session);
        } else if (role == 1 || role == 2) staffSessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
