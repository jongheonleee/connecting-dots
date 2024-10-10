package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
public class BoardController {

    @GetMapping("/list")
    public String getListPage() {
        return "boardList";
    }

    @GetMapping("/detail")
    public String getDetailPage() {
        return "boardDetail";
    }

    @GetMapping("/write")
    public String getWritePage() {
        return "boardWrite";
    }

}
