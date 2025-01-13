package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.repository.mybatis.service.ServiceUserGradeDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceUserGradeServiceImplTest {


    @InjectMocks
    private ServiceUserGradeServiceImpl serviceUserGradeService;

    @Mock
    private ServiceUserGradeDaoImpl serviceUserGradeDao;

    @Mock
    private CustomFormatter formatter;


    @BeforeEach
    void setUp() {
        assertNotNull(serviceUserGradeService);
        assertNotNull(serviceUserGradeDao);
        assertNotNull(formatter);
    }

    @DisplayName("카운팅 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20})
    void 카운팅_테스트(int cnt) {
        when(serviceUserGradeDao.count()).thenReturn(cnt);
        assertEquals(cnt, serviceUserGradeService.count());
    }
}