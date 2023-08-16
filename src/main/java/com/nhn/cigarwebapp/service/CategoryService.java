package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    void addCategory(CategoryRequest request);

    CategoryResponse getCategoryDetail(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    boolean deleteCategory(Long id);

}
