package com.example.demo.repository.mybatis.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BestViewBoardDaoImplTest {

    @Autowired
    private BoardDaoImpl boardDao;

    @Autowired
    private BestViewBoardDaoImpl target;

    @BeforeEach
    void setUp() {
        assertNotNull(target);
        assertNotNull(boardDao);

        target.deleteAll();
        boardDao.deleteAll();
    }
}