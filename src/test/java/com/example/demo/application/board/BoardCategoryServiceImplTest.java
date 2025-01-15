package com.example.demo.application.board;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardCategoryServiceImplTest {

    @InjectMocks
    private BoardCategoryServiceImpl boardCategoryService;

    @Mock
    private BoardCategoryDaoImpl boardCategoryDao;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    void setUp() {
        assertNotNull(boardCategoryService);
        assertNotNull(boardCategoryDao);
        assertNotNull(formatter);
    }

    @Test
    public void test() {
        System.out.println("de");
    }

}