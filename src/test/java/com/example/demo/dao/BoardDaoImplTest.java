package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardDaoImplTest {

    @Autowired
    private BoardDaoImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
        target.deleteAll();
    }

    @DisplayName("게시글 카운팅")
    @Test
    public void test1() {
        int count = target.count();
        assertEquals(0, count);
    }
}