package com.example.demo.application.board;

import com.example.demo.repository.mybatis.board.BestCommentBoardDaoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BestCommentBoardServiceImpl {

    private final BestCommentBoardDaoImpl bestCommentBoardDao;

}
