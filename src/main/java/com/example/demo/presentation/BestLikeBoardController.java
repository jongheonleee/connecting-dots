package com.example.demo.presentation;

import com.example.demo.application.board.BestLikeBoardServiceImpl;
import com.example.demo.validator.board.BestLikeBoardValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@AllArgsConstructor
public class BestLikeBoardController {

    private final BestLikeBoardServiceImpl bestLikeBoardService;
    private final BestLikeBoardValidator bestLikeBoardValidator;

}
