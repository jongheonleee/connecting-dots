package com.example.demo.application.board;

import com.example.demo.repository.mybatis.board.BestViewBoardDaoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BestViewBoardServiceImpl {

    private final BestViewBoardDaoImpl bestViewBoardDao;
}
