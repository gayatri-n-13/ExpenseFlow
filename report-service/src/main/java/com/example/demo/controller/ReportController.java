package com.example.demo.controller;

import com.example.demo.model.Report;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate/{userId}")
    public ResponseEntity<Report> generateReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.generateReport(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Report> getReportByUserId(@PathVariable Long userId) {
        return reportService.getReportByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}