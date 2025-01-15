package com.example.demo.repository.mybatis.board;

import com.example.demo.application.board.BoardCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board-category")
public class BoardCategoryController {

    private final BoardCategoryServiceImpl boardCategoryService;


}
