package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.service.ServiceRuleUseDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceRuleUseServiceImplTest {

    @InjectMocks
    private ServiceRuleUseServiceImpl serviceRuleUseService;

    @Mock
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @BeforeEach
    void setUp() {
        assertNotNull(serviceRuleUseService);
        assertNotNull(serviceRuleUseDao);
    }

}