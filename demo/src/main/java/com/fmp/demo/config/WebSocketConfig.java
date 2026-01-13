package com.fmp.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket(STOMP) 설정 클래스
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 클라이언트가 SockJS/STOMP 연결을 요청할 엔드포인트를 등록
     * - "/ws-chat" 경로가 WebSocket 핸드셰이크(Handshake)를 받을 실제 URL
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws-chat")             // <- JS에서 SockJS("/ws-chat") 로 연결 시도
            .setAllowedOriginPatterns("*")       // 모든 Origin을 허용 (개발용)
            .withSockJS();                       // SockJS fallback 활성화
    }

    /**
     * 메시지 브로커 설정
     * - 클라이언트가 메시지를 보낼 때 prefix: "/app"
     * - 서버가 브로드캐스트할 때 prefix: "/topic"
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 "stompClient.send('/app/xxx', ...)" 로 보낼 때
        registry.setApplicationDestinationPrefixes("/app");
        // 클라이언트가 "/topic/xxx" 을 구독(subscribe)하면 SimpleBroker가 브로드캐스트함
        registry.enableSimpleBroker("/topic");
    }
}
