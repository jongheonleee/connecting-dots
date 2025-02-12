package com.example.demo.controller.service;

import com.example.demo.service.service.ServiceTermsService;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceTermsRequest;
import com.example.demo.dto.service.ServiceTermsResponse;
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
@RequestMapping("/api/service-terms")
public class ServiceTermsController {

    private final ServiceTermsService serviceTermsService;

    @PostMapping("/create")
    public ResponseEntity<ServiceTermsResponse> create(@Valid @RequestBody ServiceTermsRequest request) {
        ServiceTermsResponse response = serviceTermsService.create(request);
        return ResponseEntity.created(URI.create("/api/service-terms/" + response.getPoli_stat()))
                             .body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ServiceTermsResponse>> readBySearchCondition(SearchCondition sc) {
        PageResponse<ServiceTermsResponse> response = serviceTermsService.readBySearchCondition(sc);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{poli_stat}")
    public ResponseEntity<ServiceTermsResponse> read(@PathVariable("poli_stat") String poli_stat) {
        return ResponseEntity.ok(serviceTermsService.readByPoliStat(poli_stat));
    }

    @PatchMapping("/modify/{poli_stat}")
    public ResponseEntity<Void> modify(@PathVariable("poli_stat") String poli_stat, @Valid @RequestBody ServiceTermsRequest request) {
        serviceTermsService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove/{poli_stat}")
    public ResponseEntity<Void> remove(@PathVariable("poli_stat") String poli_stat) {
        serviceTermsService.remove(poli_stat);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/remove-all")
    public ResponseEntity<Void> removeAll() {
        serviceTermsService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }
}
