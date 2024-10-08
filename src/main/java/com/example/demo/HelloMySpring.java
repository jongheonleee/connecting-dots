package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloMySpring {

    @GetMapping("/")
    public String hello() {
        System.out.println("Hello, Spring!");
        return "main";
    }
}
