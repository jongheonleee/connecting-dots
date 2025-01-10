package com.example.demo.repository.mybatis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Code;
import com.example.demo.repository.mybatis.code.CommonCodeDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ServiceTermsDaoImplTest {

    @Autowired
    private ServiceTermsDaoImpl serviceTermsDao;

    @Autowired
    private ServiceRuleUseDaoImpl serviceRuleUseDao;

    @Autowired
    private CommonCodeDaoImpl commonCodeDao;

    @BeforeEach
    void setUp() {
        assertNotNull(serviceTermsDao);
        assertNotNull(serviceRuleUseDao);
        assertNotNull(commonCodeDao);

        serviceTermsDao.deleteAll();
        serviceRuleUseDao.deleteAll();
        for (int i= Code.MAX_LEVEL; i>=0; i--) {
            commonCodeDao.deleteByLevel(i);
        }

        // 서비스 이용 규칙 테스트용 데이터 3개 추가


        // 공통 코드 1개 추가
    }


}