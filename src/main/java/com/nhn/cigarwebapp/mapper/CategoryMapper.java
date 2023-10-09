package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.category.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.category.CategoryResponse;
import com.nhn.cigarwebapp.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
