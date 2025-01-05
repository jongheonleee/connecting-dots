package com.example.demo.presentation;

import com.example.demo.application.board.BestCommentBoardServiceImpl;
import com.example.demo.validator.board.BestCommentBoardValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@AllArgsConstructor
public class BestCommentBoardController {

    private final BestCommentBoardServiceImpl bestCommentBoardService;
    private final BestCommentBoardValidator bestCommentBoardValidator;


}
