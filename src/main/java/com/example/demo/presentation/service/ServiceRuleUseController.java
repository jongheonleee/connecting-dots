package com.example.demo.presentation.service;

import com.example.demo.application.service.ServiceRuleUseServiceImpl;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.service.ServiceRuleUseRequest;
import com.example.demo.dto.service.ServiceRuleUseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 1차 엔드포인트 개발
// - 페이지네이션 처리 추가[]
// - 추후에 예외처리 및 테스트 코드 강화 적용 계획[]
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-rule-use")
public class ServiceRuleUseController {

    private final ServiceRuleUseServiceImpl serviceRuleUseService;

    @PostMapping("/create")
    public ResponseEntity<ServiceRuleUseResponse> create(@Valid @RequestBody ServiceRuleUseRequest request) {
        ServiceRuleUseResponse response = serviceRuleUseService.create(request);
        return ResponseEntity.created(URI.create("/api/service-rule-use/" + response.getRule_stat()))
                             .body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ServiceRuleUseResponse>> readAll(SearchCondition sc) {
        PageResponse<ServiceRuleUseResponse> response = serviceRuleUseService.readBySearchCondition(sc);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/{code}")
    public ResponseEntity<List<ServiceRuleUseResponse>> readByCode(@PathVariable("code") @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.") String code) {
        return ResponseEntity.ok(serviceRuleUseService.readByCode(code));
    }

    @GetMapping("/{rule_stat}")
    public ResponseEntity<ServiceRuleUseResponse> read(@PathVariable("rule_stat") @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.") String rule_stat) {
        return ResponseEntity.ok(serviceRuleUseService.readByRuleStat(rule_stat));
    }

    @PatchMapping("/modify/{rule_stat}")
    public ResponseEntity<Void> modify(@PathVariable("rule_stat") @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.") String rule_stat, @Valid @RequestBody ServiceRuleUseRequest request) {
        serviceRuleUseService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{rule_stat}")
    public ResponseEntity<Void> remove(@PathVariable("rule_stat") @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "rule_stat은 대문자 2글자와 숫자 4자리로 구성되어야 합니다.") String rule_stat) {
        serviceRuleUseService.removeByRuleStat(rule_stat);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{code}")
    public ResponseEntity<Void> removeByCode(@PathVariable("code") @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.") String code) {
        serviceRuleUseService.removeByCode(code);
        return ResponseEntity.noContent()
                             .build();
    }



}
