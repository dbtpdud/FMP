// src/main/java/com/fmp/demo/controller/MatchingController.java

package com.fmp.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmp.demo.service.MatchingService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    @GetMapping("/random")
    public ResponseEntity<Map<String, String>> randomMatch(HttpSession session) {
        // 1) 기존 세션에 userId가 없으면 새로 생성
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
            session.setAttribute("userId", userId);
        }
        // 2) MatchingService에 userId 보내서 방 ID 리턴받기
        String roomId = matchingService.joinQueueAndMatch(userId);

        // 3) JSON 응답에 roomId는 물론 userId도 함께 담아 보낸다
        Map<String, String> result = new HashMap<>();
        result.put("roomId", roomId);   // 매칭이 아직 안됐으면 null, 되었으면 방 ID
        result.put("userId", userId);   // 나의 userId를 클라이언트가 알 수 있게
        return ResponseEntity.ok(result);
    }
}
