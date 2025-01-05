package com.example.demo.presentation;

import com.example.demo.application.board.BestViewBoardServiceImpl;
import com.example.demo.validator.board.BestViewBoardValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메인 개발 목록
 * - 주간 베스트 게시글 목록 조회
 *  - 1. 조회수 상위 12개
 *  - 2. 좋아요 상위 12개
 *  - 3. 댓글 수 상위 12개
 *
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/board/best-view")
public class BestViewBoardController {

    private final BestViewBoardServiceImpl bestViewBoardService;
    private final BestViewBoardValidator bestViewBoardValidator;
}
