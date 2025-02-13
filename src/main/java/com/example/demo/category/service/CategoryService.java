package com.example.demo.category.service;

import com.example.demo.category.dto.CategoryDto;
import com.example.demo.category.model.Category;
import com.example.demo.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CategoryDto saveCategory(CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    private CategoryDto convertToDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setParent(category.getParent() != null ? new CategoryDto() : null);
        if (category.getParent() != null) {
            categoryDto.getParent().setId(category.getParent().getId());
            categoryDto.getParent().setName(category.getParent().getName());
        }
        categoryDto.setChildren(category.getChildren() != null ? category.getChildren().stream().map(this::convertToDto).collect(Collectors.toList()) : null);
        return categoryDto;
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setParent(categoryDto.getParent() != null ? new Category() : null);
        if (categoryDto.getParent() != null) {
            category.getParent().setId(categoryDto.getParent().getId());
            category.getParent().setName(categoryDto.getParent().getName());
        }
        category.setChildren(categoryDto.getChildren() != null ? categoryDto.getChildren().stream().map(this::convertToEntity).collect(Collectors.toList()) : null);
        return category;
    }
}