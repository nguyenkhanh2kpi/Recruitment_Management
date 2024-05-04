package com.java08.quanlituyendung.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SessionMessage {
    private Map<String, List<String>> sessionMessages = new HashMap<>();

    public void addMessage(String sessionId, String message) {
        List<String> messages = sessionMessages.getOrDefault(sessionId, new ArrayList<>());
        messages.add(message);
        sessionMessages.put(sessionId, messages);
    }

    public void removeSession(String sessionId) {
        sessionMessages.remove(sessionId);
    }

    public List<String> getSessionMessages(String sessionId) {
        return sessionMessages.getOrDefault(sessionId, new ArrayList<>());
    }

    public Map<String, List<String>> getSessionMessages() {
        return sessionMessages;
    }
}
