package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.CategoryMapper;
import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.request.category.CategoryRequest;
import com.nhn.cigarwebapp.model.response.category.CategoryResponse;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .collect(Collectors.toList());
    }

    public void addCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.saveAndFlush(category);
    }

}
