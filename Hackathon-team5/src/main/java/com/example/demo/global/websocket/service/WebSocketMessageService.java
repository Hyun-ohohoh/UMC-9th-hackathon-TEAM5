package com.example.demo.global.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketMessageService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 특정 방의 모든 참가자에게 이벤트 메시지 전송
     * @param roomId 방 ID
     * @param eventType 이벤트 타입
     * @param responseData API 응답 데이터
     */
    public void sendEventToRoom(Long roomId, String eventType, Object responseData) {
        String destination = "/topic/room/" + roomId;

        WebSocketMessage message = WebSocketMessage.builder()
                .eventType(eventType)
                .data(responseData)
                .build();

        messagingTemplate.convertAndSend(destination, message);

        System.out.println("WebSocket 메시지 전송 - roomId: " + roomId + ", eventType: " + eventType);
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class WebSocketMessage {
        private String eventType;
        private Object data;
    }
}
