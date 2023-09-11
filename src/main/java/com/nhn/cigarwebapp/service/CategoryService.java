package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    List<CategoryResponse> getAdminCategories(Map<String, String> params);

    void addCategory(CategoryRequest request);

    CategoryResponse getCategoryDetail(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    boolean deleteCategory(Long id);

}
