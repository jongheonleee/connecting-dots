package com.example.demo.application.service.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.application.service.comment.CommentServiceImpl;
import com.example.demo.dto.comment.CommentDto;
import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentDaoImpl commentDao;


    @InjectMocks
    private CommentServiceImpl target;


    private List<CommentDto> fixture = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        assertNotNull(commentDao);
        assertNotNull(target);
    }

    /**
     * - 0. 카운팅
     *
     * - 1.
     */

}