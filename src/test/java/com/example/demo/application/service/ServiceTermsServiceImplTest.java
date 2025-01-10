package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.repository.mybatis.service.ServiceTermsDaoImpl;
import com.example.demo.utils.CustomFormatter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ServiceTermsServiceImplTest {

    @InjectMocks
    private ServiceTermsServiceImpl serviceTermsService;

    @Mock
    private ServiceTermsDaoImpl serviceTermsDao;

    @Mock
    private CustomFormatter formatter;


    

}