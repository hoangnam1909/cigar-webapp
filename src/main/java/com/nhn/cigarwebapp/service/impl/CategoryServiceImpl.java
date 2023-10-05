package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.mapper.CategoryMapper;
import com.nhn.cigarwebapp.mapper.SortMapper;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.service.CategoryService;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.category.CategorySpecification;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SpecificationMapper specificationMapper;
    private final SortMapper sortMapper;

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
    public Page<CategoryResponse> getAdminCategories(Map<String, String> params) {
        int PAGE_SIZE = 15;

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : PAGE_SIZE;

        CategorySpecification specification = specificationMapper.categorySpecification(params);
        Pageable pageable = PageRequest.of(page - 1, size);

        return categoryRepository.findAll(specification, pageable)
                .map(categoryMapper::toResponse);
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
