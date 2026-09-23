package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CategoryClient {

    @Autowired
    private RestTemplate restTemplate;

    public boolean categoryExists(Long categoryId) {
        try {
            String url = "http://category-service/categories/" + categoryId;
            Map response = restTemplate.getForObject(url, Map.class);
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }
}