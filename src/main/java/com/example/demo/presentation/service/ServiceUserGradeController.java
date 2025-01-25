package com.example.demo.presentation.service;

import com.example.demo.application.service.impl.ServiceUserGradeServiceImpl;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceUserGradeRequest;
import com.example.demo.dto.service.ServiceUserGradeResponse;
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
@RequestMapping("/api/service-user-grade")
public class ServiceUserGradeController {

    private final ServiceUserGradeServiceImpl serviceUserGradeService;

    @PostMapping("/create")
    public ResponseEntity<ServiceUserGradeResponse> create(@Valid @RequestBody ServiceUserGradeRequest request) {
        ServiceUserGradeResponse response = serviceUserGradeService.create(request);
        return ResponseEntity.created(URI.create("/api/service-user-grade/" + response.getStat_code()))
                             .body(response);
    }

    @GetMapping("/{stat_code}")
    public ResponseEntity<ServiceUserGradeResponse> read(@PathVariable("stat_code") String stat_code) {
        return ResponseEntity.ok(serviceUserGradeService.readByStatCode(stat_code));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ServiceUserGradeResponse>> readBySearchCondition(SearchCondition sc) {
        PageResponse<ServiceUserGradeResponse> response = serviceUserGradeService.readBySearchCondition(sc);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{stat_code}")
    public ResponseEntity<Void> modify(@PathVariable("stat_code") String stat_code, @Valid @RequestBody ServiceUserGradeRequest request) {
        request.setStat_code(stat_code);
        serviceUserGradeService.modify(request);
        return ResponseEntity.ok()
                             .build();
    }

    @DeleteMapping("/{stat_code}")
    public ResponseEntity<Void> remove(@PathVariable("stat_code") String stat_code) {
        serviceUserGradeService.removeByStatCode(stat_code);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> removeAll() {
        serviceUserGradeService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }

}
