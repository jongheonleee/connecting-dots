package com.example.demo.application.board;

import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardCategoryServiceImpl {

    private final BoardCategoryDaoImpl boardCategoryDao;
    private final CustomFormatter formatter;



}
