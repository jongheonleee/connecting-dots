package com.example.demo.controller.report;

import com.example.demo.dto.report.ReportProcessDetailsRequest;
import com.example.demo.dto.report.ReportProcessDetailsResponse;
import com.example.demo.service.report.ReportProcessDetailsService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report-process-details")
public class ReportProcessDetailsController {

    private final ReportProcessDetailsService reportProcessDetailsService;
    
    @PostMapping("/renew")
    public ResponseEntity<ReportProcessDetailsResponse> renew(@Valid @RequestBody final ReportProcessDetailsRequest request) {
        ReportProcessDetailsResponse response = reportProcessDetailsService.renew(request);
        return ResponseEntity.created(URI.create("/api/report-process-details/renew" + response.getSeq()))
                             .body(response);
    }

}
