package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.CategoryDto;
import com.example.demo.services.CategoryService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable("catId") Integer categoryId) {
        CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("catId") Integer categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse("Category is deleted successfully", true), HttpStatus.OK);
    }

    // ✅ GET SINGLE CATEGORY
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("catId") Integer categoryId) {
        CategoryDto categoryDto = this.categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    // ✅ GET ALL CATEGORIES (with Pagination)
    @GetMapping("/")
    public ResponseEntity<Object> getAllCategories(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Page<CategoryDto> allCategories = this.categoryService.getAllCategories(PageRequest.of(page, size));
        
        // Prepare response with additional pagination data
        return new ResponseEntity<>(new PaginationResponse(allCategories.getContent(), allCategories.getTotalElements(), allCategories.getTotalPages()), HttpStatus.OK);
    }

    // 🔍 SEARCH CATEGORIES BY TITLE (Query Parameter)
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategory(@RequestParam String keyword) {
        List<CategoryDto> results = this.categoryService.searchCategories(keyword);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // GET CATEGORY BY TITLE
    @GetMapping("/title/{title}")
    public ResponseEntity<CategoryDto> getCategoryByTitle(@PathVariable String title) {
        CategoryDto categoryDto = this.categoryService.getCategory(title);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    // Pagination Response DTO
    public static class PaginationResponse {
        private List<CategoryDto> categories;
        private long totalElements;
        private int totalPages;

        public PaginationResponse(List<CategoryDto> categories, long totalElements, int totalPages) {
            this.categories = categories;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }

        public List<CategoryDto> getCategories() {
            return categories;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}
