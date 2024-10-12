package com.example.demo.service;

import com.example.demo.dao.CategoryDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {

    private CategoryDaoImpl categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDaoImpl categoryRepository) {
        this.categoryDao = categoryRepository;
    }



}
