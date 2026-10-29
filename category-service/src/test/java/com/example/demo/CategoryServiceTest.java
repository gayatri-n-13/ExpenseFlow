package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void addCategory_savesAndReturnsCategory() {
        Category food = new Category(null, "Food");
        Category saved = new Category(1L, "Food");
        when(categoryRepository.save(food)).thenReturn(saved);

        Category result = categoryService.addCategory(food);

        assertEquals(1L, result.getCategoryId());
        assertEquals("Food", result.getName());
        verify(categoryRepository).save(food);
    }

    @Test
    void getAllCategories_returnsAllFromRepository() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                new Category(1L, "Food"),
                new Category(2L, "Travel")));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Travel", result.get(1).getName());
    }

    @Test
    void getCategoryById_whenExists_returnsCategory() {
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(new Category(1L, "Food")));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals("Food", result.get().getName());
    }

    @Test
    void getCategoryById_whenMissing_returnsEmpty() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(999L);

        assertTrue(result.isEmpty());
    }
}