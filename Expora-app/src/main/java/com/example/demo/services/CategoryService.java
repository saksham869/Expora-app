package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.payloads.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

    CategoryDto getCategory(Integer categoryId);  // Get by title

    void deleteCategory(Integer categoryId);

    List<CategoryDto> getAllCategories();

    List<CategoryDto> searchCategories(String keyword);

	CategoryDto getCategory(String title);

	Page<CategoryDto> getAllCategories(PageRequest of);

    // Removed getCategoryByCategoryTitle because getCategory(String title) does the same job
}
