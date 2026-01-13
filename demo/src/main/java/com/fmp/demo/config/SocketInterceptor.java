package com.fmp.demo.config;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.fmp.demo.repository.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// (세션에서 로그인 사용자만 허용, userId를 WebSocket 속성에 저장)
@Component
public class SocketInterceptor implements HandshakeInterceptor {

	  @Override
	    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

	        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
	        HttpSession session = req.getSession(false);
	        if (session != null) {
	            User user = (User) session.getAttribute("principal");
	            if (user != null) {
	                attributes.put("userId", user.getStudentId()); // WebSocketSession에서 userId 사용 가능
	                return true;
	            }
	        }
	        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
	        return false; // 로그인 안됐으면 연결 거부
	    }

	    @Override
	    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
	                              WebSocketHandler wsHandler, Exception exception) {
	        // 필요시 로그/후처리
	    }
	
}
