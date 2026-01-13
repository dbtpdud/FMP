package com.fmp.demo.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatHandler extends TextWebSocketHandler {

    // 연결된 유저 세션 관리 (studentId → WebSocketSession)
    private static final Map<String, WebSocketSession> sessions = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("웹소켓 연결됨: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode node = mapper.readTree(payload);

        // 1. 최초 연결 시 userId 등록(type: ENTER)
        if (node.has("type") && "ENTER".equals(node.get("type").asText())) {
            String userId = node.get("userId").asText();
            session.getAttributes().put("userId", userId);
            sessions.put(userId, session);
            System.out.println("userId 등록: " + userId);
            return;
        }

        // 2. 채팅 메시지 (to: 상대ID, msg: 내용)
        String toUserId = node.has("to") ? node.get("to").asText() : null;
        String msg = node.has("msg") ? node.get("msg").asText() : null;
        String fromUserId = (String) session.getAttributes().get("userId");
        long timestamp = System.currentTimeMillis();

        if (toUserId != null && msg != null) {
            WebSocketSession toSession = sessions.get(toUserId);
            if (toSession != null && toSession.isOpen()) {
                // JSON 형태로 메시지 포장해서 상대에게 전달
                Map<String, Object> resp = new HashMap<>();
                resp.put("from", fromUserId);
                resp.put("msg", msg);
                resp.put("timestamp", timestamp);
                String json = mapper.writeValueAsString(resp);
                toSession.sendMessage(new TextMessage(json));
            }
            // 내 메시지도(자기 화면에도) 똑같이 출력
            Map<String, Object> myResp = new HashMap<>();
            myResp.put("from", fromUserId);
            myResp.put("msg", msg);
            myResp.put("timestamp", timestamp);
            String myJson = mapper.writeValueAsString(myResp);
            session.sendMessage(new TextMessage(myJson));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("연결 종료: " + userId);
        }
    }
}
