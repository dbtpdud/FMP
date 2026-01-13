package com.fmp.demo.controller;

import com.fmp.demo.repository.model.User;
import com.fmp.demo.service.MypageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    @Autowired
    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    /**
     * 1) 마이페이지 조회
     * GET /mypage
     */
    @GetMapping
    public String showMypage(Model model, HttpSession session) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId == null) {
            // 로그인 전 상태라면 로그인 페이지로
            return "redirect:/login";
        }
        User user = mypageService.getUserInfo(studentId);
        model.addAttribute("user", user);
        return "mypage";  // templates/mypage.html
    }

    /**
     * 2) 마이페이지 정보 수정
     * POST /mypage/update
     */
    @PostMapping("/update")
    public String updateUser(
            @RequestParam("phone") String phone,
            @RequestParam("department") String department,
            HttpSession session
    ) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId == null) {
            return "redirect:/login";
        }

        // 기존 사용자 정보 불러오기
        User existingUser = mypageService.getUserInfo(studentId);

        // 새로운 user 객체에 수정 정보 + 유지할 값 세팅
        User user = new User();
        user.setStudentId(studentId);
        user.setPhone(phone);
        user.setDepartment(department);
        user.setMajor(existingUser.getMajor()); 

        mypageService.updateUserInfo(user);
        return "redirect:/mypage";
    }


    /**
     * 3) 회원 탈퇴
     * GET /mypage/delete
     */
    @GetMapping("/delete")
    public String deleteUser(HttpSession session) {
        String studentId = (String) session.getAttribute("studentId");
        if (studentId != null) {
            mypageService.withdrawUser(studentId); // 하드 삭제
            session.invalidate(); // 로그아웃 처리
        }
        return "redirect:/"; // 메인 페이지로 바로 이동
    }

    
    /**
     * 4) 로그아웃
     * GET /mypage/logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

}