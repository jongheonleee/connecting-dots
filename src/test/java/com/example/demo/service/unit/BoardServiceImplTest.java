package com.example.demo.service.unit;

import com.example.demo.dao.BoardDaoImpl;
import com.example.demo.service.BoardServiceImpl;
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