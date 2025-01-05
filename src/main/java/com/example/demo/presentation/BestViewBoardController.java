package com.example.demo.presentation;

import com.example.demo.application.board.BestViewBoardServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BestViewBoardDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BestViewBoardDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.validator.board.BestViewBoardValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메인 개발 목록
 * - 주간 베스트 게시글 목록 조회
 *  - 1. 조회수 상위 12개
 *  - 2. 조회수 상위 12개
 *  - 3. 댓글 수 상위 12개
 *
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/board/best-view")
public class BestViewBoardController {

    private final BestViewBoardServiceImpl bestViewBoardService;
    private final BestViewBoardValidator bestViewBoardValidator;


    @InitBinder({"bestViewBoardUpdateDto", "bestViewBoardDto"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(bestViewBoardValidator);
    }


    @GetMapping("/total-count")
    public ResponseEntity<Integer> getBestViewTotalCount() {
        return ResponseEntity.ok(bestViewBoardService.count());
    }

    @GetMapping("/used-count")
    public ResponseEntity<Integer> getBestViewUsedCount() {
        return ResponseEntity.ok(bestViewBoardService.countUsed());
    }

    // save-used?cnt=10
    @GetMapping("/save-used")
    public ResponseEntity<Void> saveBestViewBoards(Integer cnt) {
        bestViewBoardService.saveForBestViewBoards(cnt);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBestViewBoard(@RequestBody @Valid BestViewBoardDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .build();
        }

        bestViewBoardService.save(dto);
        return ResponseEntity.ok("베스트 조회수 게시글이 저장되었습니다.");
    }

    // read-view?page=1&size=10
    @GetMapping("/read-view")
    public ResponseEntity<List<BoardResponseDto>> readForView(Map<String, Object> map, SearchCondition sc) {
        map.put("pageSize", sc.getPageSize());
        map.put("offset", sc.getOffset());
        return ResponseEntity.ok(bestViewBoardService.readForView(map));
    }

    // read?seq=123
    @GetMapping("/read")
    public ResponseEntity<BestViewBoardDto> readBestViewBoard(Integer seq) {
        return ResponseEntity.ok(bestViewBoardService.read(seq));
    }

    // read-all
    @GetMapping("/read-all")
    public ResponseEntity<List<BestViewBoardDto>> readAllBestViewBoards() {
        return ResponseEntity.ok(bestViewBoardService.readAll());
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyBestViewBoard(@RequestBody @Valid BestViewBoardUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .build();
        }

        bestViewBoardService.modify(dto);
        return ResponseEntity.ok("베스트 조회수 게시글이 수정되었습니다.");
    }

    // modify-used?up_id=123
    @PutMapping("/modify-used")
    public ResponseEntity<String> modifyBestViewBoardUsed(String up_id) {
        bestViewBoardService.modifyUsed(up_id);
        return ResponseEntity.ok("베스트 조회수 게시글이 수정되었습니다.");
    }

}
