package com.example.demo.presentation.board;

import com.example.demo.application.board.BestCommentBoardServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BestCommentBoardDto;
import com.example.demo.dto.board.BestCommentBoardUpdateDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.validator.board.BestCommentBoardValidator;
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
@RequestMapping("/board/best-comment")
public class BestCommentBoardController {

    private final BestCommentBoardServiceImpl bestCommentBoardService;
    // 추후에 RestAPI 형태로 바꿀 계획이라 validator 부분 활용하지 말기
    private final BestCommentBoardValidator bestCommentBoardValidator;


    @InitBinder({"bestCommentBoardUpdateDto", "bestCommentBoardDto"})
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(bestCommentBoardValidator);
    }


    @GetMapping("/total-count")
    public ResponseEntity<Integer> getBestCommentTotalCount() {
        return ResponseEntity.ok(bestCommentBoardService.count());
    }

    @GetMapping("/used-count")
    public ResponseEntity<Integer> getBestCommentUsedCount() {
        return ResponseEntity.ok(bestCommentBoardService.countUsed());
    }

    // save-used?cnt=10
    @GetMapping("/save-used")
    public ResponseEntity<Void> saveBestCommentBoards(Integer cnt) {
        bestCommentBoardService.saveForBestCommentBoards(cnt);
        return ResponseEntity.ok()
                             .build();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBestCommentBoard(@RequestBody @Valid BestCommentBoardDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                                 .build();
        }

        bestCommentBoardService.save(dto);
        return ResponseEntity.ok("베스트 댓글 게시글이 저장되었습니다.");
    }

    // read-view?page=1&size=10
    @GetMapping("/read-view")
    public ResponseEntity<List<BoardResponseDto>> readForView(Map<String, Object> map, SearchCondition sc) {
        map.put("pageSize", sc.getPageSize());
        map.put("offset", sc.getOffset());
        return ResponseEntity.ok(bestCommentBoardService.readForView(map));
    }

    // read?seq=123
    @GetMapping("/read")
    public ResponseEntity<BestCommentBoardDto> readBestCommentBoard(Integer seq) {
        return ResponseEntity.ok(bestCommentBoardService.read(seq));
    }

    // read-all
    @GetMapping("/read-all")
    public ResponseEntity<List<BestCommentBoardDto>> readAllBestCommentBoards() {
        return ResponseEntity.ok(bestCommentBoardService.readAll());
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyBestCommentBoard(@RequestBody @Valid BestCommentBoardUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                                 .build();
        }

        bestCommentBoardService.modify(dto);
        return ResponseEntity.ok("베스트 댓글 게시글이 수정되었습니다.");
    }

    // modify-used?up_id=123
    @PutMapping("/modify-used")
    public ResponseEntity<String> modifyBestCommentBoardUsed(String up_id) {
        bestCommentBoardService.modifyUsed(up_id);
        return ResponseEntity.ok("베스트 댓글 게시글이 수정되었습니다.");
    }
}
