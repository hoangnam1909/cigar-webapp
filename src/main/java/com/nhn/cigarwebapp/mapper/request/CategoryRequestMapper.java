package com.nhn.cigarwebapp.mapper.request;

import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.request.category.CategoryRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryRequestMapper implements Function<CategoryRequest, Category> {

    @Override
    public Category apply(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .build();
    }

}
