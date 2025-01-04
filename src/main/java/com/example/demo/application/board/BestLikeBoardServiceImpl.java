package com.example.demo.application.board;

import com.example.demo.repository.mybatis.board.BestLikeBoardDaoImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BestLikeBoardServiceImpl {

    private final BestLikeBoardDaoImpl bestLikeBoardDao;
}
