package com.example.demo.presentation.service;

import com.example.demo.application.service.ServiceUserConditionServiceImpl;
import com.example.demo.dto.service.ServiceUserConditionRequest;
import com.example.demo.dto.service.ServiceUserConditionResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/service-user-condition")
public class ServiceUserConditionController {

    private final ServiceUserConditionServiceImpl serviceUserConditionService;

    @PostMapping("/create")
    public ResponseEntity<ServiceUserConditionResponse> craete(@Valid @RequestBody ServiceUserConditionRequest request) {
        ServiceUserConditionResponse response = serviceUserConditionService.create(request);
        return ResponseEntity.created(URI.create("/api/service-user-condition/" + response.getCond_code()))
                             .body(response);
    }

    @GetMapping("/{cond_code}")
    public ResponseEntity<ServiceUserConditionResponse> read(@PathVariable("cond_code") String cond_code) {
        return ResponseEntity.ok(serviceUserConditionService.readByCondCode(cond_code));
    }

    @PatchMapping("/modify/{cond_code}")
    public ResponseEntity<Void> modify(@PathVariable("cond_code") String cond_code, @Valid @RequestBody ServiceUserConditionRequest request) {
        serviceUserConditionService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{cond_code}")
    public ResponseEntity<Void> remove(@PathVariable("cond_code") String cond_code) {
        serviceUserConditionService.remove(cond_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeAll() {
        serviceUserConditionService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }
}
