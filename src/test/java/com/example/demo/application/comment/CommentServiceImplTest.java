package com.example.demo.application.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl sut;

    @Mock
    private CommentDaoImpl commentDao;

    @Mock
    private CustomFormatter formatter;

    @BeforeEach
    public void setUp() {
        assertNotNull(sut);
        assertNotNull(commentDao);
        assertNotNull(formatter);
    }
}