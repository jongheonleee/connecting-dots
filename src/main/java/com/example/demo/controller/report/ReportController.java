package com.example.demo.controller.report;

import com.example.demo.dto.report.ReportDetailResponse;
import com.example.demo.dto.report.ReportRequest;
import com.example.demo.dto.report.ReportResponse;
import com.example.demo.service.report.ReportService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/detail/{rno}")
    public ResponseEntity<ReportDetailResponse> readDetail(@PathVariable("rno") final Integer rno) {
        ReportDetailResponse response = reportService.readReportDetailsByRno(rno);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ReportResponse> create(
            @Valid @RequestBody final ReportRequest request
    ) {
        ReportResponse response = reportService.create(request);
        return ResponseEntity.created(URI.create("/api/report/create" + response.getRno()))
                             .body(response);
    }

    @PutMapping("/modify")
    public ResponseEntity<Void> modify(
            @Valid @RequestBody final ReportRequest request
    ) {
        reportService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<Void> removeByRno(@PathVariable("rno") final Integer rno) {
        reportService.removeByRno(rno);
        return ResponseEntity.noContent()
                             .build();
    }

}
