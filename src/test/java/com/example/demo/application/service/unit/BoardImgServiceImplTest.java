package com.example.demo.application.service.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.application.board.BoardImgServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardImgServiceImplTest {

    @Autowired
    private BoardImgServiceImpl target;

    @BeforeEach
    void setUp() {
        assertNotNull(target);
    }

    @Test
    public void test() {
        System.out.println("de");
    }
}