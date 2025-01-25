package com.example.demo.controller.service;


import com.example.demo.service.service.ServiceUserConditionsService;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserConditionsDetailResponse;
import com.example.demo.dto.service.ServiceUserConditionsRequest;
import com.example.demo.dto.service.ServiceUserConditionsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-user-conditions")
public class ServiceUserConditionsController {

    private final ServiceUserConditionsService serviceUserConditionsService;

    @PostMapping("/create")
    public ResponseEntity<ServiceUserConditionsResponse> create(@Valid @RequestBody ServiceUserConditionsRequest request) {
        ServiceUserConditionsResponse response = serviceUserConditionsService.create(request);
        return ResponseEntity.created(URI.create("/api/service-user-condition/" + response.getConds_code()))
                             .body(response);
    }

    @GetMapping("/{conds_code}")
    public ResponseEntity<ServiceUserConditionsResponse> read(@PathVariable("conds_code") String conds_code) {
        return ResponseEntity.ok(serviceUserConditionsService.readByCondsCode(conds_code));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ServiceUserConditionsResponse>> readBySearchCondition(SearchCondition sc) {
        PageResponse<ServiceUserConditionsResponse> response = serviceUserConditionsService.readBySearchCondition(sc);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{conds_code}")
    public ResponseEntity<ServiceUserConditionsDetailResponse> readByCondsCodeDetail(@PathVariable("conds_code") String conds_code) {
        return ResponseEntity.ok(serviceUserConditionsService.readByCondsCodeForUserConditions(conds_code));
    }

    @PatchMapping("/modify/{conds_code}")
    public ResponseEntity<Void> modify(@PathVariable("conds_code") String conds_code, @Valid @RequestBody ServiceUserConditionsRequest request) {
        serviceUserConditionsService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{conds_code}")
    public ResponseEntity<Void> remove(@PathVariable("conds_code") String conds_code) {
        serviceUserConditionsService.removeByCondsCode(conds_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeAll() {
        serviceUserConditionsService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }
}
