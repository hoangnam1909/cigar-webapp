package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.category.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.category.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    Page<CategoryResponse> getAdminCategories(Map<String, String> params);

    void addCategory(CategoryRequest request);

    CategoryResponse getCategory(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    boolean deleteCategory(Long id);

}
