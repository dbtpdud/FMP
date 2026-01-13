package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.fmp.demo.repository.interfaces.UserRepository;


@Controller
public class FindController {
	
	@Autowired
	private UserRepository userRepository;

    @GetMapping("/find-account")
    public String findAccountPage() {
        return "findAccount"; //  templates/findAccount.html 이라는 파일을 찾아서 보여줌.
    }
    @PostMapping("/find-id")
    @ResponseBody
    public String findUserId(@RequestParam("name") String name, @RequestParam("phone") String phone) {
        String userId = userRepository.findUserIdByNameAndPhone(name, phone);
        return userId != null ? "아이디는: " + userId : "일치하는 회원이 없습니다.";
    }

    @PostMapping("/find-password")
    @ResponseBody
    public String findPassword(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("phone") String phone) {
        String password = userRepository.findPassword(userId, name, phone);
        return password != null ? "비밀번호는: " + password : "입력하신 정보와 일치하는 비밀번호가 없습니다.";
    }
    
}
