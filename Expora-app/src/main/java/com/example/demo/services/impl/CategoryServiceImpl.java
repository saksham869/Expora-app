package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.CategoryDto;
import com.example.demo.repositries.CategoryRepo;
import com.example.demo.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    private CategoryDto mapCategoryToDto(Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepo.save(category);
        return this.mapCategoryToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCategory = this.categoryRepo.save(category);
        return this.mapCategoryToDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategory(String title) {
        Category category = this.categoryRepo.findByCategoryTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "title", title));
        return this.mapCategoryToDto(category);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        this.categoryRepo.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepo.findAll();
        return categories.stream()
                .map(this::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> searchCategories(String keyword) {
        List<Category> categories = categoryRepo.findByCategoryTitleContainingIgnoreCase(keyword);
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Category", "keyword", keyword);
        }
        return categories.stream()
                .map(this::mapCategoryToDto)
                .collect(Collectors.toList());
    }

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<CategoryDto> getAllCategories(PageRequest of) {
		// TODO Auto-generated method stub
		return null;
	}
}
