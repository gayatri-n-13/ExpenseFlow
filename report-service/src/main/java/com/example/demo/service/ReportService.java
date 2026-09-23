package com.example.demo.service;

import com.example.demo.client.ExpenseClient;
import com.example.demo.model.ExpenseDTO;
import com.example.demo.model.Report;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ExpenseClient expenseClient;

    @Autowired
    private ReportRepository reportRepository;

    public Report generateReport(Long userId) {
        List<ExpenseDTO> expenses = expenseClient.getExpensesByUserId(userId);

        double total = expenses.stream()
                .mapToDouble(ExpenseDTO::getAmount)
                .sum();

        Optional<Report> existing = reportRepository.findByUserId(userId);
        Report report = existing.orElse(new Report(userId, total));
        report.setTotalExpense(total);

        return reportRepository.save(report);
    }

    public Optional<Report> getReportByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}