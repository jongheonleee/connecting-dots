//package com.example.demo.presentation.comment;
//
//import static org.springframework.http.HttpStatus.*;
//
//import com.example.demo.global.error.exception.business.comment.CommentFormInvalidException;
//import com.example.demo.global.error.exception.business.comment.CommentNotFoundException;
//import com.example.demo.application.comment.CommentServiceImpl;
//import com.example.demo.validator.comment.CommentValidator;
//import com.example.demo.dto.ord_comment.CommentResponseDto;
//import com.example.demo.dto.ord_comment.CommentRequestDto;
//import jakarta.validation.Valid;
//import java.util.List;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 댓글 개발 목록
// *
// * - 1. 댓글 작성 ✅
// * - 2. 댓글 조회 ✅
// * - 3. 댓글 수정 ✅
// * - 4. 댓글 삭제 ✅
// * - 5. 댓글 좋아요/싫어요 ✅
// *
// **/
//
//
//@RestController
//@RequestMapping("/comments")
//public class CommentController {
//
//    private final CommentServiceImpl commentService;
//    private final CommentValidator commentValidator;
//
//    public CommentController(CommentServiceImpl commentService, CommentValidator commentValidator) {
//        this.commentService = commentService;
//        this.commentValidator = commentValidator;
//    }
//
//    @InitBinder("commentRequestDto")
//    public void initBinder(WebDataBinder binder) {
//        binder.addValidators(commentValidator);
//    }
//
//    @ExceptionHandler({CommentFormInvalidException.class, CommentNotFoundException.class})
//    public ResponseEntity<String> handleCommentException(Exception ex) {
//        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
//    }
//
//    // 댓글 등록, 수정, 삭제, 조회 처리
//    @GetMapping("/all")
//    public ResponseEntity<List<CommentResponseDto>> getCommentList() {
//        List<CommentResponseDto> selectedAllComment = commentService.findAll();
//        return ResponseEntity.status(OK)
//                             .body(selectedAllComment);
//    }
//
//    // 댓글 등록
//    @PostMapping("/write")
//    public ResponseEntity<String> write(@RequestBody @Valid CommentRequestDto commentRequestDto, BindingResult result, Errors errors) {
//        if (result.hasErrors()) {
//            return ResponseEntity.status(BAD_REQUEST)
//                                 .body("댓글 등록에 실패했습니다.");
//        }
//
//        commentService.create(commentRequestDto);
//        return ResponseEntity.status(CREATED)
//                             .body("댓글이 등록되었습니다.");
//    }
//
//    // 댓글 삭제
//    @GetMapping("/{cno}")
//    public ResponseEntity<String> remove(@PathVariable("cno") Integer cno) {
//        commentService.removeByCno(cno);
//        return ResponseEntity.status(OK)
//                             .body("댓글이 삭제되었습니다.");
//    }
//
//    // 댓글 수정
//    @PostMapping("/modify/{cno}")
//    public ResponseEntity<String> modify(@PathVariable("cno") Integer cno, @RequestBody @Valid CommentRequestDto commentRequestDto, BindingResult result) {
//        if (result.hasErrors()) {
//            return ResponseEntity.status(BAD_REQUEST)
//                                 .body("댓글 수정에 실패했습니다.");
//        }
//
//        commentRequestDto.setCno(cno);
//        commentService.update(commentRequestDto);
//        return ResponseEntity.status(OK)
//                             .body("댓글이 수정되었습니다.");
//    }
//
//    // 댓글 좋아요 처리
//    @PostMapping("/like")
//    public ResponseEntity<String> like(@RequestBody Integer cno) {
//        commentService.increaseLikeCnt(cno);
//        return ResponseEntity.status(OK)
//                             .body("댓글 좋아요 처리가 되었습니다.");
//    }
//
//    // 댓글 싫어요 처리
//    @PostMapping("/dislike")
//    public ResponseEntity<String> dislike(@RequestBody Integer cno) {
//        commentService.increaseDislikeCnt(cno);
//        return ResponseEntity.status(OK)
//                             .body("댓글 싫어요 처리가 되었습니다.");
//    }
//
//
//}
