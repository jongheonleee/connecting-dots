package com.example.demo.application.category;

import com.example.demo.repository.mybatis.category.CategoryDaoImpl;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.exception.category.CategoryAlreadyExistsException;
import com.example.demo.exception.category.CategoryFormInvalidException;
import com.example.demo.exception.category.CategoryNotFoundException;
import com.example.demo.exception.global.InternalServerError;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class CategoryServiceImpl {

    private CategoryDaoImpl categoryDao;

//    @Autowired
    public CategoryServiceImpl(CategoryDaoImpl categoryRepository) {
        this.categoryDao = categoryRepository;
    }

    public int count() {
        try {
            return categoryDao.count();
        } catch (Exception e) {
            throw new InternalServerError();
        }
    }

    public void create(CategoryDto dto) {
        try {
            int rowCnt = categoryDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerError();
            }
        } catch (DuplicateKeyException e) {
            throw new CategoryAlreadyExistsException();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryFormInvalidException();
        }
    }

    public CategoryDto findByCode(String code) {
        var found = categoryDao.selectByCode(code);

        if (found == null) {
            throw new CategoryNotFoundException();
        }

        return found;
    }

    public List<CategoryDto> findAll() {
        return categoryDao.selectAll();
    }

    public void modify(CategoryDto dto) {
        try {
            int rowCnt = categoryDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerError();
            }
        } catch (DuplicateKeyException e) {
            throw new CategoryAlreadyExistsException();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryFormInvalidException();
        }
    }

    public void remove(String code) {
        if (findByCode(code) == null) {
            throw new CategoryNotFoundException();
        }

        int rowCnt = categoryDao.deleteByCode(code);

        if (rowCnt != 1) {
            throw new InternalServerError();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = count();
        int rowCnt = categoryDao.deleteAll();

        if (totalCnt != rowCnt) {
            throw new InternalServerError();
        }
    }

    public List<CategoryDto> findAllTopCategory() {
        return categoryDao.selectAllTopCategory();
    }

    public List<CategoryDto> findAllByTopCate(String top_cate) {
        return categoryDao.selectAllByTopCate(top_cate);
    }

    public List<CategoryDto> findTopAndSubCategory() {
        List<CategoryDto> categories = categoryDao.selectAllTopCategory();
        for (var topCategory : categories) {
            String topCate = topCategory.getCate_code();
            List<CategoryDto> subCategories = categoryDao.selectAllByTopCate(topCate);
            topCategory.setSubCategories(subCategories);
        }

        return categories;

    }



}
