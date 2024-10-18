package com.example.demo.presentation;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDetailDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.application.service.board.BoardServiceImpl;
import com.example.demo.application.service.category.CategoryServiceImpl;
import com.example.demo.application.validator.board.BoardValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/board")
@Controller
public class BoardController {

    private CategoryServiceImpl categoryService;
    private BoardServiceImpl boardService;
    private BoardValidator boardValidator;

    @Autowired
    public BoardController(CategoryServiceImpl categoryService, BoardServiceImpl boardService, BoardValidator boardValidator) {
        this.categoryService = categoryService;
        this.boardService = boardService;
        this.boardValidator = boardValidator;
    }

    @InitBinder("boardFormDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(boardValidator);
    }

    @GetMapping("/list")
    public String getListPage(HttpServletRequest request, SearchCondition sc, Model model) {
        // 로그인 여부 확인 - 스프링 시큐리티로 빼기
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }
        System.out.println("sc = " + sc);

        findCategories();
        findBoardsBySearchCondition(model, sc);
        return "boardList";
    }

    @GetMapping("/detail/{bno}")
    public String getDetailPage(HttpServletRequest request, @PathVariable("bno") Integer bno, Model model) {
        // 로그인 여부 확인 - 스프링 시큐리티로 빼기
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }

        findBoardDetailByBno(model, bno);
        return "boardDetail";
    }

    @GetMapping("/list/{cate_code}")
    public String getListPageByCateCode(@PathVariable("cate_code") String cate_code, Model model) {
        findBoardsByCateCode(model, cate_code);
        return "boardList";
    }

    @GetMapping("/write")
    public String getWritePage(HttpServletRequest request, @ModelAttribute("boardFormDto") BoardFormDto boardFormDto) {
        // 로그인 여부 확인 - 스프링 시큐리티로 빼기
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }

        findCategories();
        return "boardWrite";
    }


    @PostMapping("/write")
    public String write(HttpServletRequest request, @Valid @ModelAttribute("boardFormDto") BoardFormDto boardFormDto, BindingResult result,
            @RequestParam("boardImgFile") List<MultipartFile> boardImgFiles) {
        // 로그인 여부 확인 - 스프링 시큐리티로 빼기
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI() != null ? request.getRequestURI() : "/";
            return "redirect:/user/login?toUrl=" + toUrl;
        }

        // 데이터 유효성 검증
        if (result.hasErrors()) {
            return "boardWrite";
        }


        // 세션에 등록된 아이디로 작성자, 아이디 필드 채우기
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("id");
        boardFormDto.setId(userId);
        boardFormDto.setWriter(userId);


        boardService.create(boardFormDto, boardImgFiles);
        return "redirect:/board/list";
    }

    private boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null && session.getAttribute("id") != null;
    }

    private @ModelAttribute("categories") List<CategoryDto> findCategories() {
        return categoryService.findTopAndSubCategory();
    }


    private void findBoardsBySearchCondition(Model model, SearchCondition sc) {
        List<BoardFormDto> foundBoards = boardService.findBySearchCondition(sc);
        model.addAttribute("boards", foundBoards);
    }

    private void findBoardDetailByBno(Model model, Integer bno) {
        BoardDetailDto foundDetailBoard = boardService.findDetailByBno(bno);
        model.addAttribute("boardDetail", foundDetailBoard);
    }

    private void findBoardsByCateCode(Model model, String cateCode) {
        List<BoardFormDto> foundBoards = boardService.findByCategory(cateCode);
        model.addAttribute("boards", foundBoards);
    }

}
