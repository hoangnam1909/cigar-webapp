package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.mapper.CategoryMapper;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> categoryMapper.toResponse(category))
                .sorted(Comparator.comparing(CategoryResponse::getId))
                .collect(Collectors.toList());
    }

    public void addCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public CategoryResponse getCategoryDetail(Long id) {
        System.err.println("call getCategoryDetail");
        Optional<Category> category = categoryRepository.findById(id);

        return category.map(value -> categoryMapper.toResponse(value)).orElse(null);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category categoryEditing = category.get();
            categoryEditing.setName(request.name());
            categoryRepository.save(categoryEditing);
            return categoryMapper.toResponse(categoryEditing);
        }

        return null;
    }

    @Override
    public boolean deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
