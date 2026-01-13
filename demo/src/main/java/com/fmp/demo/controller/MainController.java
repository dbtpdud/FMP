package com.fmp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
