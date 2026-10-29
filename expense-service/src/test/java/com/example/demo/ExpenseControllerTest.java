package com.example.demo;
import com.example.demo.controller.ExpenseController;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.model.Expense;
import com.example.demo.service.ExpenseService;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getExpensesByUser_returnsList() throws Exception {
        when(expenseService.getExpensesByUserId(1L)).thenReturn(
                List.of(new Expense(1L, 500.0, LocalDate.of(2026, 10, 29), 2L)));

        mockMvc.perform(get("/expenses/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(500.0));
    }

    @Test
    void postExpense_withInvalidCategory_returns400() throws Exception {
        when(expenseService.addExpense(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new RuntimeException("Invalid categoryId: 999"));

        mockMvc.perform(post("/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"amount\":500,\"date\":\"2026-10-29\",\"categoryId\":999}"))
                .andExpect(status().isBadRequest());
    }
}