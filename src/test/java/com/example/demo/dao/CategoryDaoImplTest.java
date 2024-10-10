package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryDaoImplTest {

    @Autowired
    private CategoryDaoImpl target;

    @BeforeEach
    public void setUp() {
        assertNotNull(target);
    }


}