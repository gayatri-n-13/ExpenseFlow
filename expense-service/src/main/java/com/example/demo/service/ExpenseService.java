package com.example.demo.service;

import com.example.demo.client.CategoryClient;
import com.example.demo.model.Expense;
import com.example.demo.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryClient categoryClient;

    public Expense addExpense(Expense expense) {
        boolean valid = categoryClient.categoryExists(expense.getCategoryId());
        if (!valid) {
            throw new RuntimeException("Invalid categoryId: " + expense.getCategoryId());
        }
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }
}