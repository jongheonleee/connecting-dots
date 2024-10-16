package com.example.demo.controller;

import com.example.demo.dto.BoardFormDto;
import com.example.demo.dto.CategoryDto;
import com.example.demo.service.BoardServiceImpl;
import com.example.demo.service.CategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/board")
@Controller
public class BoardController {

    private CategoryServiceImpl categoryService;
    private BoardServiceImpl boardService;

    @Autowired
    public BoardController(CategoryServiceImpl categoryService, BoardServiceImpl boardService) {
        this.categoryService = categoryService;
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public String getListPage(HttpServletRequest request, Model model) {
        // 로그인 안되있으면 로그인 요청하기
        // 이때, 이전의 url 정보 저장해서 리다이렉션으로 보냄
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }
        getCategories();
        List<BoardFormDto> foundBoard = boardService.findAll();
        for (BoardFormDto boardFormDto : foundBoard) {
            System.out.println("boardFormDto = " + boardFormDto);
        }
        model.addAttribute("boards", foundBoard);

        return "boardList";
    }

    @GetMapping("/detail/{bno}")
    public String getDetailPage(HttpServletRequest request, @PathVariable("bno") Integer bno) {
        // 로그인 안되있으면 로그인 요청하기
        // 이때, 이전의 url 정보 저장해서 리다이렉션으로 보냄
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }



        return "boardDetail";
    }

    @GetMapping("/write")
    public String getWritePage(HttpServletRequest request, @ModelAttribute("boardFormDto") BoardFormDto boardFormDto) throws JsonProcessingException {
        // 로그인 안되있으면 로그인 요청하기
        // 이때, 이전의 url 정보 저장해서 리다이렉션으로 보냄
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }

        getCategoriesJson();
        return "boardWrite";
    }


    @PostMapping("/write")
    public String write(BoardFormDto boardFormDto, @RequestParam("boardImgFile") List<MultipartFile> boardImgFiles) {
        System.out.println("boardFormDto = " + boardFormDto);
        boardFormDto.setId("test");
        boardFormDto.setCate_code("0101");
        System.out.println("boardImgFiles = " + boardImgFiles);
        boardService.create(boardFormDto, boardImgFiles);
        return "redirect:/board/list";
    }

    private boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null && session.getAttribute("id") != null;
    }

    private @ModelAttribute("categories") List<CategoryDto> getCategories() {
        return categoryService.findTopAndSubCategory();
    }

    private @ModelAttribute("categoriesJson") String getCategoriesJson() throws JsonProcessingException {
        List<CategoryDto> categories = categoryService.findTopAndSubCategory();
        return new ObjectMapper().writeValueAsString(categories);
    }

}
