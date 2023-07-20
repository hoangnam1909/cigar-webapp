package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.request.category.CategoryRequest;
import com.nhn.cigarwebapp.model.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    void addCategory(CategoryRequest request);

}
