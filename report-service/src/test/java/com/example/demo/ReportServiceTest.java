package com.example.demo;
import com.example.demo.service.ReportService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.client.ExpenseClient;
import com.example.demo.model.ExpenseDTO;
import com.example.demo.model.Report;
import com.example.demo.repository.ReportRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ExpenseClient expenseClient;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    private ExpenseDTO expense(double amount) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setAmount(amount);
        return dto;
    }

    @Test
    void generateReport_sumsAllExpenses() {
        when(expenseClient.getExpensesByUserId(1L))
                .thenReturn(List.of(expense(500.0), expense(300.0)));
        when(reportRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(reportRepository.save(any(Report.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Report report = reportService.generateReport(1L);

        assertEquals(800.0, report.getTotalExpense());
        assertEquals(1L, report.getUserId());
    }

    @Test
    void generateReport_withNoExpenses_totalIsZero() {
        when(expenseClient.getExpensesByUserId(2L)).thenReturn(List.of());
        when(reportRepository.findByUserId(2L)).thenReturn(Optional.empty());
        when(reportRepository.save(any(Report.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Report report = reportService.generateReport(2L);

        assertEquals(0.0, report.getTotalExpense());
    }
}