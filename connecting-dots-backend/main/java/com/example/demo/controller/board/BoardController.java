package com.example.demo.controller.board;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDetailResponse;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.BoardUpdateRequest;
import com.example.demo.service.board.BoardService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<PageResponse> read(Integer page, Integer pageSize) {
        var response = boardService.readForMain(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cate_code}")
    public ResponseEntity<PageResponse> readByCategory(Integer page, Integer pageSize, @PathVariable("cate_code") String cate_code) {
        var response = boardService.readByCategoryForMain(cate_code, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse> readBySearchCondition(SearchCondition sc) {
        var response = boardService.readBySearchConditionForMain(sc);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{bno}")
    public ResponseEntity<BoardDetailResponse> readDetailByBno(@PathVariable("bno") Integer bno) {
        var response = boardService.readDetailByBno(bno);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/like/{bno}")
    public ResponseEntity<Void> increaseReco(@PathVariable("bno") Integer bno) {
        boardService.increaseReco(bno);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/dislike/{bno}")
    public ResponseEntity<Void> increaseNotReco(@PathVariable("bno") Integer bno) {
        boardService.increaseNotReco(bno);
        return ResponseEntity.noContent()
                             .build();
    }


    @PostMapping("/create")
    public ResponseEntity<BoardResponse> create(
            @RequestPart("boardData") @Valid BoardRequest request, // JSON 데이터
            @RequestParam(value = "files", required = false) List<MultipartFile> files) { // 파일
        var response = boardService.create(request, files);
        return ResponseEntity.created(URI.create("/api/board/create" + response.getBno()))
                .body(response);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modify(
            @RequestPart("boardData") @Valid BoardUpdateRequest request, // JSON 데이터
            @RequestParam(value = "files", required = false) List<MultipartFile> files) { // 파일
        boardService.modify(request, files);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{bno}")
    public ResponseEntity<Void> remove(@PathVariable("bno") Integer bno) {
        boardService.remove(bno);
        return ResponseEntity.noContent()
                             .build();
    }




}
