package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardImgDaoImplLearningTest {
    @Autowired
    private BoardImgDaoImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
    }

    @Test
    public void testCount() {
        int count = target.count();
        assertNotNull(count);
    }
}