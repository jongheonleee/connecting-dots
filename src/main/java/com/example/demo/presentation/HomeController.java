package com.example.demo.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메인 개발 목록
 * - 주간 베스트 게시글 목록 조회
 *  - 1. 조회수 상위 12개
 *  - 2. 좋아요 상위 12개
 *  - 3. 댓글 수 상위 12개
 *
 */

@Controller
public class HomeController {

    @GetMapping("/")
    public String hello() {
        return "main";
    }
}
