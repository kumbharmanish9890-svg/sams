package com.sams.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.sams.service.ReportService;

@Controller
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/injury/report")
    public ResponseEntity<byte[]> injuryReport() {
        byte[] pdf = reportService.injuryReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SAMS_Injury_Report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}