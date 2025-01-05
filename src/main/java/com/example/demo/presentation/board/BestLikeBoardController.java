package com.example.demo.presentation.board;

import com.example.demo.application.board.BestLikeBoardServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BestLikeBoardDto;
import com.example.demo.dto.board.BestLikeBoardUpdateDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.validator.board.BestLikeBoardValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/board/best-like")
public class BestLikeBoardController {

    private final BestLikeBoardServiceImpl bestLikeBoardService;
    private final BestLikeBoardValidator bestLikeBoardValidator;


    @InitBinder({"bestLikeBoardUpdateDto", "bestLikeBoardDto"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(bestLikeBoardValidator);
    }


    @GetMapping("/total-count")
    public ResponseEntity<Integer> getBestLikeTotalCount() {
        return ResponseEntity.ok(bestLikeBoardService.count());
    }

    @GetMapping("/used-count")
    public ResponseEntity<Integer> getBestLikeUsedCount() {
        return ResponseEntity.ok(bestLikeBoardService.countUsed());
    }

    // save-used?cnt=10
    @GetMapping("/save-used")
    public ResponseEntity<Void> saveBestLikeBoards(Integer cnt) {
        bestLikeBoardService.saveForBestLikeBoards(cnt);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBestLikeBoard(@RequestBody @Valid BestLikeBoardDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .build();
        }

        bestLikeBoardService.save(dto);
        return ResponseEntity.ok("베스트 좋아요 게시글이 저장되었습니다.");
    }

    // read-view?page=1&size=10
    @GetMapping("/read-view")
    public ResponseEntity<List<BoardResponseDto>> readForView(Map<String, Object> map, SearchCondition sc) {
        map.put("pageSize", sc.getPageSize());
        map.put("offset", sc.getOffset());
        return ResponseEntity.ok(bestLikeBoardService.readForView(map));
    }

    // read?seq=123
    @GetMapping("/read")
    public ResponseEntity<BestLikeBoardDto> readBestLikeBoard(Integer seq) {
        return ResponseEntity.ok(bestLikeBoardService.read(seq));
    }

    // read-all
    @GetMapping("/read-all")
    public ResponseEntity<List<BestLikeBoardDto>> readAllBestLikeBoards() {
        return ResponseEntity.ok(bestLikeBoardService.readAll());
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyBestLikeBoard(@RequestBody @Valid BestLikeBoardUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .build();
        }

        bestLikeBoardService.modify(dto);
        return ResponseEntity.ok("베스트 좋아요 게시글이 수정되었습니다.");
    }

    // modify-used?up_id=123
    @PutMapping("/modify-used")
    public ResponseEntity<String> modifyBestLikeBoardUsed(String up_id) {
        bestLikeBoardService.modifyUsed(up_id);
        return ResponseEntity.ok("베스트 좋아요 게시글이 수정되었습니다.");
    }
}
