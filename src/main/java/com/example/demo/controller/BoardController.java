package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.service.CategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
public class BoardController {

    private CategoryServiceImpl categoryService;

    @Autowired
    public BoardController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getListPage(Model model) {
        getCategories();

        return "boardList";
    }

    @GetMapping("/detail")
    public String getDetailPage() {

        return "boardDetail";
    }

    @GetMapping("/write")
    public String getWritePage(Model model) throws JsonProcessingException {
        getCategoriesJson();
        return "boardWrite";
    }

    private @ModelAttribute("categories") List<CategoryDto> getCategories() {
        return categoryService.findTopAndSubCategory();
    }

    private @ModelAttribute("categoriesJson") String getCategoriesJson() throws JsonProcessingException {
        List<CategoryDto> categories = categoryService.findTopAndSubCategory();
        return new ObjectMapper().writeValueAsString(categories);
    }

}
