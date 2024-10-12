package com.example.demo.service;

import com.example.demo.dao.CategoryDaoImpl;
import com.example.demo.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl {

    private CategoryDaoImpl categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDaoImpl categoryRepository) {
        this.categoryDao = categoryRepository;
    }

    public int count() {
        try {
            return categoryDao.count();
        } catch (Exception e) {
            throw new InternalServerError("예기치 못한 에러");
        }
    }




}
