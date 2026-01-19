package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 메인 controller
 */

@Controller
public class MainController {

	// http://localhost:8080/
    @GetMapping("/")
    public String index() {
        return "index";  // src/main/resources/templates/index.html
    }


}
