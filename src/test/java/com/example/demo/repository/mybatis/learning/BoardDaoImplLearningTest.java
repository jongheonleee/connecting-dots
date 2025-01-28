package com.example.demo.repository.mybatis.learning;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.repository.board.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardDaoImplLearningTest {

    @Autowired
    private BoardRepository target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
    }

    @Test
    public void test1() {
        int count = target.count();
        System.out.println(count);
    }



}