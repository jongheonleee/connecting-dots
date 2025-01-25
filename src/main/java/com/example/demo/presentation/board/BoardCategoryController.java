package com.example.demo.presentation.board;


import com.example.demo.application.board.impl.BoardCategoryServiceImpl;
import com.example.demo.dto.board.BoardCategoryRequest;
import com.example.demo.dto.board.BoardCategoryResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board-category")
public class BoardCategoryController {

    private final BoardCategoryServiceImpl boardCategoryService;

    @PostMapping("/create")
    public ResponseEntity<BoardCategoryResponse> create(@Valid @RequestBody BoardCategoryRequest request) {
        BoardCategoryResponse response = boardCategoryService.create(request);
        return ResponseEntity.created(URI.create("/api/board-category/" + response.getCate_code()))
                             .body(response);
    }

    @GetMapping("/{cate_code}")
    public ResponseEntity<BoardCategoryResponse> read(@PathVariable("cate_code") String cate_code) {
        return ResponseEntity.ok(boardCategoryService.readByCateCode(cate_code));
    }

    @GetMapping("/top/{top_cate}")
    public ResponseEntity<List<BoardCategoryResponse>> readByTopCode(@PathVariable("top_cate") String top_cate) {
        return ResponseEntity.ok(boardCategoryService.readByTopCate(top_cate));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BoardCategoryResponse>> readAll() {
        return ResponseEntity.ok(boardCategoryService.readAll());
    }

    @PutMapping("/{cate_code}")
    public ResponseEntity<Void> modify(@PathVariable("cate_code") String cate_code, @Valid @RequestBody BoardCategoryRequest request) {
        boardCategoryService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping("/usey/{cate_code}")
    public ResponseEntity<Void> modifyChkUseY(@PathVariable("cate_code") String cate_code) {
        boardCategoryService.modifyChkUseY(cate_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping("/usen/{cate_code}")
    public ResponseEntity<Void> modifyChkUseN(@PathVariable("cate_code") String cate_code) {
        boardCategoryService.modifyChkUseN(cate_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{cate_code}")
    public ResponseEntity<Void> remove(@PathVariable("cate_code") String cate_code) {
        boardCategoryService.remove(cate_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> removeAll() {
        boardCategoryService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }
}
