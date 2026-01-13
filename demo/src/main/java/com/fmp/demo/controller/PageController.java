package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 로그인 페이지로 이동
@Controller
public class PageController {
    @GetMapping("/login")
    public String loginPage() {
        return "signIn";
    }
    
    @GetMapping("/match")
    public String matchPage() {
    	return "match";
    }
}
