package com.example.demo.presentation;

import com.example.demo.application.exception.comment.CommentFormInvalidException;
import com.example.demo.application.exception.comment.CommentNotFoundException;
import com.example.demo.application.service.comment.CommentServiceImpl;
import com.example.demo.application.validator.comment.CommentValidator;
import com.example.demo.dto.comment.CommentResponseDto;
import com.example.demo.dto.comment.CommentRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentServiceImpl commentService;
    private final CommentValidator commentValidator;

    public CommentController(CommentServiceImpl commentService, CommentValidator commentValidator) {
        this.commentService = commentService;
        this.commentValidator = commentValidator;
    }

    @InitBinder("commentRequestDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(commentValidator);
    }

    @ExceptionHandler({CommentFormInvalidException.class, CommentNotFoundException.class})
    public ResponseEntity<String> handleCommentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 댓글 등록, 수정, 삭제, 조회 처리

    @GetMapping("/all")
    public ResponseEntity<String> getCommentList() {
        return null;
    }

    @GetMapping("/{bno}")
    public ResponseEntity<List<CommentResponseDto>> getCommentListByBno(@PathVariable("bno") Integer bno) {
        List<CommentResponseDto> foundComments = commentService.findByBno(bno);
        for (CommentResponseDto foundComment : foundComments) {
            System.out.println("foundComment = " + foundComment);
        }
        return null;
    }

    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody @Valid CommentRequestDto commentRequestDto, BindingResult result, Errors errors) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 등록에 실패했습니다.");
        }

        commentService.create(commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 등록되었습니다.");
    }

    @GetMapping("/{cno}")
    public ResponseEntity<String> remove(@PathVariable("cno") Integer cno) {
        return null;
    }

    @PostMapping("/modify/{cno}")
    public ResponseEntity<String> modify(@PathVariable("cno") Integer cno, CommentResponseDto commentResponseDto) {
        return null;
    }

    @PostMapping("/like")
    public ResponseEntity<String> like(@RequestBody Integer cno) {
        return null;
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislike(@RequestBody Integer cno) {
        return null;
    }


}
