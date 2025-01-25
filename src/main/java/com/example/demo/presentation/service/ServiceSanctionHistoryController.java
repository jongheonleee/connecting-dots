package com.example.demo.presentation.service;


import com.example.demo.application.service.impl.ServiceSanctionHistoryServiceImpl;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.service.ServiceSanctionHistoryRequest;
import com.example.demo.dto.service.ServiceSanctionHistoryResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/api/service-sanction-history")
public class ServiceSanctionHistoryController {

    private final ServiceSanctionHistoryServiceImpl serviceSanctionHistoryService;

    @PostMapping("/create")
    public ResponseEntity<ServiceSanctionHistoryResponse> create(@Valid @RequestBody ServiceSanctionHistoryRequest request) {
        ServiceSanctionHistoryResponse response = serviceSanctionHistoryService.create(request);
        return ResponseEntity.created(URI.create("/api/service-sanction-history/" + response.getSeq()))
                             .body(response);
    }

    @GetMapping("/{seq}")
    public ResponseEntity<ServiceSanctionHistoryResponse> read(@PathVariable("seq") Integer seq) {
        return ResponseEntity.ok(serviceSanctionHistoryService.readBySeq(seq));
    }

    @GetMapping("/list")
    public ResponseEntity<PageResponse<ServiceSanctionHistoryResponse>> read(SearchCondition sc) {
        return ResponseEntity.ok(serviceSanctionHistoryService.readBySearchCondition(sc));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceSanctionHistoryResponse>> readAll() {
        return ResponseEntity.ok(serviceSanctionHistoryService.readAll());
    }

    @GetMapping("/all/{user_seq}")
    public ResponseEntity<List<ServiceSanctionHistoryResponse>> readAllByUserSeq(@PathVariable("user_seq") Integer user_seq) {
        return ResponseEntity.ok(serviceSanctionHistoryService.readByUserSeq(user_seq));
    }

    @GetMapping("/list/{user_seq}")
    public ResponseEntity<List<ServiceSanctionHistoryResponse>> readByUserSeq(@PathVariable("user_seq") Integer user_seq) {
        return ResponseEntity.ok(serviceSanctionHistoryService.readByUserSeq(user_seq));
    }

    @PatchMapping("/modify/{seq}")
    public ResponseEntity<Void> modify(@PathVariable("seq") Integer seq, @Valid @RequestBody ServiceSanctionHistoryRequest request) {
        request.setSeq(seq);
        serviceSanctionHistoryService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> remove(@PathVariable("seq") Integer seq) {
        serviceSanctionHistoryService.remove(seq);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/list/{user_seq}")
    public ResponseEntity<Void> removeByUserSeq(@PathVariable("user_seq") Integer user_seq) {
        serviceSanctionHistoryService.removeByUserSeq(user_seq);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> removeAll() {
        serviceSanctionHistoryService.removeAll();
        return ResponseEntity.noContent()
                             .build();
    }
}
