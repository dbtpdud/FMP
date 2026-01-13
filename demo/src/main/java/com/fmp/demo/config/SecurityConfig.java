package com.fmp.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            // 1) CSRF 비활성화 (개발 단계). 프로덕션에서는 CSRF 토큰 처리 필요.
	            .csrf(csrf -> csrf.disable())

	            // 2) 요청 권한 설정
	            .authorizeHttpRequests(auth -> auth
	            		
	            		.requestMatchers("/mypage/**").permitAll()

	                // WebSocket/STOMP 엔드포인트 허용
	                .requestMatchers("/ws-chat/**", "/app/**", "/topic/**").permitAll()
	                // 매칭 API 및 정적 리소스 허용
	                .requestMatchers("/match/**", "/chat.html", "/dm/**", "/static/**").permitAll()
	                // 나머지 요청은 인증 필요 없이 허용(원한다면 .authenticated()로 변경)
	                .anyRequest().permitAll()
	            )

	            // 3) H2 콘솔 등을 위해 frameOptions 비활성화 (필요 없으면 이 줄 삭제 가능)
	            .headers(headers -> headers.frameOptions().disable());

	            // 4) 기본 로그인 폼 비활성화 (필요하다면 주석처리)
//	            .httpBasic(Customizer.withDefaults());

	        return http.build();
	    }
}
