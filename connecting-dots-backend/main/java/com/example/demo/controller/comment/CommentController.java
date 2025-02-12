package com.example.demo.controller.comment;

import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import com.example.demo.service.comment.CommentService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{cno}")
    public ResponseEntity<CommentResponse> readByCno(@PathVariable("cno") Integer cno) {
        return ResponseEntity.ok(commentService.readByCno(cno));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentRequest request) {
        var response = commentService.create(request);
        return ResponseEntity.created(URI.create("/api/comment/" + response.getCno()))
                             .body(response);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modify(@RequestBody @Valid CommentRequest request) {
        commentService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/like/{cno}")
    public ResponseEntity<Void> increaseLikeCnt(@PathVariable("cno") Integer cno) {
        commentService.increaseLikeCnt(cno);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/dislike/{cno}")
    public ResponseEntity<Void> increaseDislikeCnt(@PathVariable("cno") Integer cno) {
        commentService.increaseDislikeCnt(cno);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{cno}")
    public ResponseEntity<Void> remove(@PathVariable("cno") Integer cno) {
        commentService.remove(cno);
        return ResponseEntity.noContent()
                             .build();
    }



}
