package com.fmp.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatsController {

    // ✅ 채팅 메인 페이지: /random-match
    @GetMapping("/random-match")
    public String randomMatchPage(HttpSession session, Model model) {
        String myUserId = getStudentIdFromSession(session);

        // Thymeleaf(chat.html)에서 MY_USER_ID로 쓰게 내려줌
        model.addAttribute("myUserId", myUserId);

        return "chat";
    }

    // ✅ 방(매치)별 진입 페이지
    @GetMapping("/random-match/{matchId}")
    public String chatRoom(
            @PathVariable("matchId") Long matchId,
            HttpSession session,
            Model model
    ) {
        String myUserId = getStudentIdFromSession(session);

        model.addAttribute("myUserId", myUserId);
        model.addAttribute("matchId", matchId);

        return "chat";
    }

    /**
     * ✅ 세션에서 학번(studentId)을 최대한 안전하게 찾는 함수
     * (MatchController에서 쓰던 방식과 동일 컨셉)
     */
    private String getStudentIdFromSession(HttpSession session) {
        if (session == null) return null;

        Object v;

        v = session.getAttribute("studentId");
        if (v instanceof String s && !s.isBlank()) return s;

        v = session.getAttribute("student_id");
        if (v instanceof String s && !s.isBlank()) return s;

        v = session.getAttribute("userId");
        if (v instanceof String s && !s.isBlank()) return s;

        v = session.getAttribute("user_id");
        if (v instanceof String s && !s.isBlank()) return s;

        return null;
    }
}
