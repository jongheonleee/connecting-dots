package com.example.demo.presentation;

import com.example.demo.application.board.BestLikeBoardServiceImpl;
import com.example.demo.validator.board.BestLikeBoardValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/board/best-like")
public class BestLikeBoardController {

    private final BestLikeBoardServiceImpl bestLikeBoardService;
    private final BestLikeBoardValidator bestLikeBoardValidator;

}
