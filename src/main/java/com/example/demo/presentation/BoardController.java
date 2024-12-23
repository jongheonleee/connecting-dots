package com.example.demo.presentation;

import com.example.demo.application.exception.board.BoardFormInvalidException;
import com.example.demo.application.exception.board.BoardImgNotFoundException;
import com.example.demo.application.exception.board.BoardNotFoundException;
import com.example.demo.application.exception.global.InternalServerError;
import com.example.demo.application.service.comment.CommentServiceImpl;
import com.example.demo.dto.PageHandler;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final CategoryServiceImpl categoryService;
    private final BoardServiceImpl boardService;
    private final BoardValidator boardValidator;
    private final CommentServiceImpl commentService;

    public BoardController(CategoryServiceImpl categoryService, BoardServiceImpl boardService,
            BoardValidator boardValidator, CommentServiceImpl commentService) {
        this.categoryService = categoryService;
        this.boardService = boardService;
        this.boardValidator = boardValidator;
        this.commentService = commentService;
    }

    @InitBinder({"boardFormDto", "updatedBoardFormDto"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(boardValidator);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BoardNotFoundException.class, BoardImgNotFoundException.class, BoardFormInvalidException.class})
    public String handleBoardException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @GetMapping("/list")
    public String getListPage(SearchCondition sc, Model model) {
        findCategories(model);
        findBoardsBySearchCondition(model, sc);
        createPageHandlerBySearchCondition(model, sc);
        return "boardList";
    }

    @GetMapping("/detail/{bno}")
    public String getDetailPage(@PathVariable("bno") Integer bno, Model model) {
        findBoardDetailByBno(model, bno);
        findCommentByBno(model, bno);
        return "boardDetail";
    }

    @GetMapping("/list/{cate_code}")
    public String getListPageByCateCode(@PathVariable("cate_code") String cate_code, Model model) {
        findCategories(model);
        createPageHandlerByCateCode(model, cate_code);
        findBoardsByCateCode(model, cate_code);
        return "boardList";
    }

    @GetMapping("/write")
    public String getWritePage(@ModelAttribute("boardFormDto") BoardFormDto boardFormDto, Model model) {
        findCategories(model);
        return "boardWrite";
    }


    @PostMapping("/write")
    public String write(@Valid @ModelAttribute("boardFormDto") BoardFormDto boardFormDto, BindingResult result,
            @RequestParam("boardImgFile") List<MultipartFile> boardImgFiles, @SessionAttribute String id) {
        if (result.hasErrors())
            return "boardWrite";

        boardFormDto.setId(id);
        boardFormDto.setWriter(id);
        boardService.create(boardFormDto, boardImgFiles);
        return "redirect:/board/list";
    }

    @GetMapping("/modify/{bno}")
    public String getModifyPage(@PathVariable("bno") Integer bno, Model model) {
        findCategories(model);
        findBoardByBnoForUpdate(model, bno);
        return "boardModify";
    }

    @PostMapping("/modify/{bno}")
    public String modify(@Valid @ModelAttribute("updatedBoardFormDto") BoardFormDto updatedBoardFormDto, BindingResult result,
            @RequestParam("boardImgFile") List<MultipartFile> boardImgFiles) {
        if (result.hasErrors())
            return "boardModify";


        boardService.modify(updatedBoardFormDto, boardImgFiles);
        return "redirect:/board/list";
    }

    @GetMapping("/remove/{bno}")
    public String remove(@PathVariable("bno") Integer bno) {
        boardService.remove(bno);
        return "redirect:/board/list";
    }

    @ResponseBody
    @PostMapping("/like")
    public ResponseEntity<String> like(@RequestBody Integer bno) {
        boardService.increaseRecoCnt(bno);
        return new ResponseEntity<>("추천이 완료되었습니다.", HttpStatus.OK);
    }



    @ResponseBody
    @PostMapping("/dislike")
    public ResponseEntity<String> dislike(@RequestBody Integer bno) {
        boardService.increaseNotRecoCnt(bno);
        return new ResponseEntity<>("비추천이 완료되었습니다.", HttpStatus.OK);
    }

    private void findCommentByBno(Model model, Integer bno) {
        var comments = commentService.findByBno(bno);
        model.addAttribute("comments", comments);
    }


    private void findCategories(Model model) {
        List<CategoryDto> foundCategories = categoryService.findTopAndSubCategory();
        model.addAttribute("categories", foundCategories);
    }


    private void findBoardsBySearchCondition(Model model, SearchCondition sc) {
        List<BoardFormDto> foundBoards = boardService.findBySearchCondition(sc);
        model.addAttribute("boards", foundBoards);
    }

    private void findBoardDetailByBno(Model model, Integer bno) {
        var foundDetailBoard = boardService.findDetailByBno(bno);
        model.addAttribute("boardDetail", foundDetailBoard);
    }

    private void findBoardByBnoForUpdate(Model model, Integer bno) {
        var foundDetailBoard = boardService.findByBno(bno);
        model.addAttribute("updatedBoardFormDto", foundDetailBoard);
    }

    private void findBoardsByCateCode(Model model, String cateCode) {
        List<BoardFormDto> foundBoards = boardService.findByCategory(cateCode);
        model.addAttribute("boards", foundBoards);
    }

    private void createPageHandlerBySearchCondition(Model model, SearchCondition sc) {
        int totalCnt = boardService.count(sc);
        PageHandler ph = new PageHandler(totalCnt, sc);
        model.addAttribute("ph", ph);
    }

    private void createPageHandlerByCateCode(Model model, String cateCode) {
        int totalCnt = boardService.count(cateCode);
        PageHandler ph = new PageHandler(totalCnt, new SearchCondition());
        model.addAttribute("ph", ph);
    }

}
