package com.example.demo;
import com.example.demo.service.ExpenseService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.client.CategoryClient;
import com.example.demo.model.Expense;
import com.example.demo.repository.ExpenseRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void addExpense_withValidCategory_savesExpense() {
        Expense expense = new Expense(1L, 500.0, LocalDate.of(2026, 10, 29), 2L);
        when(categoryClient.categoryExists(2L)).thenReturn(true);
        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense result = expenseService.addExpense(expense);

        assertEquals(500.0, result.getAmount());
        verify(expenseRepository).save(expense);
    }

    @Test
    void addExpense_withInvalidCategory_throwsAndDoesNotSave() {
        Expense expense = new Expense(1L, 500.0, LocalDate.of(2026, 10, 29), 999L);
        when(categoryClient.categoryExists(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> expenseService.addExpense(expense));

        assertTrue(ex.getMessage().contains("Invalid categoryId"));
        verify(expenseRepository, never()).save(any());
    }
}