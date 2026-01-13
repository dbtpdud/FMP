package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fmp.demo.dto.SignUpDTO;
import com.fmp.demo.repository.model.User;
import com.fmp.demo.service.UserService;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public String signupPage() {
        return "join";
    }

    @PostMapping
    public String signup(SignUpDTO dto) {
    	

        User user = new User();
        
        user.setUserId(dto.getUserId());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        
     // ✅ 성별 변환 로직
        String genderInput = dto.getGender();
        if (genderInput.equals("남자")) {
            user.setGender("M");
        } else if (genderInput.equals("여자")) {
            user.setGender("F");
        } else {
            user.setGender("M");
        }
        user.setDepartment(dto.getDepartment());
        user.setMajor(dto.getMajor());
        user.setSchoolYear(dto.getSchoolYear());
        user.setStudentId(dto.getStudentId());
        
        // 이미지 업로드는 지금은 생략
        // joinDate, deleted, verified 는 자동으로 설정됨 (@PrePersist)

        int result = userService.registerUser(user);
        return result > 0 ? "redirect:/login" : "redirect:/signup?error=true";
    }

}
