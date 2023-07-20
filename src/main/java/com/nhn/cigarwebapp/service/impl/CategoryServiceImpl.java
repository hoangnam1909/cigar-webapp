package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.request.CategoryRequestMapper;
import com.nhn.cigarwebapp.mapper.response.CategoryResponseMapper;
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
    private CategoryRequestMapper categoryRequestMapper;

    @Autowired
    private CategoryResponseMapper categoryResponseMapper;

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryResponseMapper)
                .collect(Collectors.toList());
    }

    public void addCategory(CategoryRequest request) {
        Category category = categoryRequestMapper.apply(request);
        categoryRepository.saveAndFlush(category);
    }

}