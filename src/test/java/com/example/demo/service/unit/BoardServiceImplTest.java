package com.example.demo.service.unit;

import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.application.service.board.BoardServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @Mock
    private BoardDaoImpl boardDao;

    @InjectMocks
    private BoardServiceImpl target;
}