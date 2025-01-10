package com.example.demo.presentation.code;

import com.example.demo.application.code.CommonCodeServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodePageResponse;
import com.example.demo.dto.code.CodeRequest;
import com.example.demo.dto.code.CodeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 1차 엔드포인트 개발
// - 페이지네이션 처리 추가[]
// - 추후에 예외처리 및 테스트 코드 강화 적용 계획[]
@Slf4j
@Validated
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


    @GetMapping("/list/{seq}")
    public ResponseEntity<CodeResponse> readDetailBySeq(@PathVariable("seq") Integer seq) {
        return ResponseEntity.ok(commonCodeService.readBySeq(seq));
    }

    @GetMapping("/list/{code}")
    public ResponseEntity<CodeResponse> readDetailByCode(@PathVariable @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.") String code) {
        return ResponseEntity.ok(commonCodeService.readByCode(code));
    }


    @GetMapping("/list")
    public ResponseEntity<CodePageResponse> read(SearchCondition sc) {
        return ResponseEntity.ok(commonCodeService.readBySearchCondition(sc));
    }


    @PatchMapping("/modify/{seq}")
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

    @DeleteMapping("/remove/{level}")
    public ResponseEntity<Void> removeByLevel(@PathVariable("level") Integer level) {
        commonCodeService.removeByLevel(level);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity<Void> removeByCode(@PathVariable("code") String code) {
        commonCodeService.removeByCode(code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{seq}")
    public ResponseEntity<Void> removeBySeq(@PathVariable("seq") Integer seq) {
        commonCodeService.removeBySeq(seq);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeAll() {
        commonCodeService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }


}
