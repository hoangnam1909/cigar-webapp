package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.category.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.category.CategoryResponse;
import com.nhn.cigarwebapp.entity.Category;
import com.nhn.cigarwebapp.mapper.CategoryMapper;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import com.nhn.cigarwebapp.service.CategoryService;
import com.nhn.cigarwebapp.specification.SpecificationMapper;
import com.nhn.cigarwebapp.specification.category.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SpecificationMapper specificationMapper;

    @Override
    @Cacheable(value = "List<CategoryResponse>")
    public List<CategoryResponse> getCategories() {
        CategorySpecification specification = specificationMapper.categorySpecification(new HashMap<>());
        return categoryRepository.findAll(specification)
                .stream()
                .map(categoryMapper::toResponse)
                .sorted(Comparator.comparing(CategoryResponse::getId))
                .toList();
    }

    @Override
    @Cacheable(key = "#params", value = "Page<CategoryResponse>")
    public Page<CategoryResponse> getAdminCategories(Map<String, String> params) {
        int pageSize = 15;

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : pageSize;

        CategorySpecification specification = specificationMapper.categorySpecification(params);
        Pageable pageable;
        if (page == 0)
            pageable = Pageable.unpaged();
        else
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("id")));

        return categoryRepository.findAll(specification, pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "List<CategoryResponse>", allEntries = true),
            @CacheEvict(value = "Page<CategoryResponse>", allEntries = true),
    })
    public void addCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        categoryRepository.saveAndFlush(category);
    }

    @Override
    @Cacheable(key = "#id", value = "CategoryResponse")
    public CategoryResponse getCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toResponse).orElse(null);
    }

    @Override
    @Caching(put = {
            @CachePut(key = "#id", value = "CategoryResponse")
    }, evict = {
            @CacheEvict(value = "List<CategoryResponse>", allEntries = true),
            @CacheEvict(value = "Page<CategoryResponse>", allEntries = true),
    })
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(request.getName());
            categoryRepository.save(category);

            return categoryMapper.toResponse(category);
        }

        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#id", value = "CategoryResponse"),
            @CacheEvict(value = "List<CategoryResponse>", allEntries = true),
            @CacheEvict(value = "Page<CategoryResponse>", allEntries = true),
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
