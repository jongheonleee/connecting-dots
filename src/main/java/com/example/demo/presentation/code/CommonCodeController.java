package com.example.demo.presentation.code;

import com.example.demo.application.code.CommonCodeServiceImpl;
import com.example.demo.dto.code.CodeRequest;
import com.example.demo.dto.code.CodeResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 1차 엔드포인트 개발
// - 추후에 예외처리 및 테스트 코드 강화 적용 계획
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/common-code")
public class CommonCodeController {

    private final CommonCodeServiceImpl commonCodeService;

    @PostMapping("/create")
    public ResponseEntity<CodeResponse> create(@RequestBody @Valid CodeRequest request) {
        CodeResponse response = commonCodeService.create(request);
        return ResponseEntity.created(URI.create("/api/common-code/" + response.getSeq()))
                             .body(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(commonCodeService.count());
    }

    @GetMapping("/read-top")
    public ResponseEntity<List<CodeResponse>> readByTopCode(String top_code) {
        return ResponseEntity.ok(commonCodeService.readByTopCode(top_code));
    }

    @GetMapping("/read-seq")
    public ResponseEntity<CodeResponse> readBySeq(Integer seq) {
        return ResponseEntity.ok(commonCodeService.readBySeq(seq));
    }

    @GetMapping("/read-code")
    public ResponseEntity<CodeResponse> readByCode(String code) {
        return ResponseEntity.ok(commonCodeService.readByCode(code));
    }

    @GetMapping("/read-all")
    public ResponseEntity<List<CodeResponse>> readAll() {
        return ResponseEntity.ok(commonCodeService.readAll());
    }

    @PatchMapping("/modify")
    public ResponseEntity<Void> modify(@RequestBody @Valid CodeRequest request) {
        commonCodeService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping("/modify-use")
    public ResponseEntity<Void> modifyUse(@RequestBody CodeRequest request) {
        commonCodeService.modifyUse(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove-level")
    public ResponseEntity<Void> removeByLevel(Integer level) {
        commonCodeService.removeByLevel(level);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove-code")
    public ResponseEntity<Void> removeByCode(String code) {
        commonCodeService.removeByCode(code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove-seq")
    public ResponseEntity<Void> removeBySeq(Integer seq) {
        commonCodeService.removeBySeq(seq);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove-all")
    public ResponseEntity<Void> removeAll() {
        commonCodeService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }


}
