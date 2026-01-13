package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fmp.demo.dto.UserDTO;
import com.fmp.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 로그인 controller
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 로그인 폼 띄우기 (GET)
    @GetMapping("/signIn")
    public String showLoginForm() {
        return "signIn";  // signIn.html
    }
    
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "join";   // join.html
    }

    // 로그인 처리 (POST)
    @PostMapping("/login")
    public String login(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {
        UserDTO userDTO = userService.login(userId, password);

        if (userDTO != null) {
            // ✅ 세션에 userId, studentId 저장
            session.setAttribute("userId", userDTO.getUserId());
            session.setAttribute("studentId", userDTO.getStudentId());
            return "redirect:/match";
        } else {
            // ❌ 실패 시 뷰에 error 속성으로 메시지 전달
            model.addAttribute("error", "없는 사용자이거나 비밀번호가 일치하지 않습니다.");
            return "signIn";
        }
    }
}
