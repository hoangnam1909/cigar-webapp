package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.mapper.CategoryMapper;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.service.CategoryService;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.category.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SpecificationMapper specificationMapper;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryResponse> getCategories() {
        CategorySpecification specification = specificationMapper.categorySpecification(new HashMap<>());
        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::toResponse)
                .sorted(Comparator.comparing(CategoryResponse::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#params", value = "adminCategories")
    public List<CategoryResponse> getAdminCategories(Map<String, String> params) {
        CategorySpecification specification = specificationMapper.categorySpecification(params);
        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::toResponse)
                .sorted(Comparator.comparing(CategoryResponse::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "adminCategories", allEntries = true),
    })
    public void addCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    @Cacheable(key = "#id", value = "categories")
    public CategoryResponse getCategoryDetail(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toResponse).orElse(null);
    }

    @Override
    @Caching(put = {
            @CachePut(key = "#id", value = "categories")
    }, evict = {
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "adminCategories", allEntries = true),
    })
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(request.name());
            categoryRepository.save(category);

            return categoryMapper.toResponse(category);
        }

        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", allEntries = true),
            @CacheEvict(value = "adminCategories", allEntries = true),
    })
    public boolean deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
