package com.example.demo.client;

import com.example.demo.model.ExpenseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ExpenseClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<ExpenseDTO> getExpensesByUserId(Long userId) {
        String url = "http://expense-service/expenses/user/" + userId;
        ExpenseDTO[] response = restTemplate.getForObject(url, ExpenseDTO[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }
}