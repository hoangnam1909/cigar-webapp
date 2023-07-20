package com.nhn.cigarwebapp.mapper.response;

import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.response.category.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class CategoryResponseMapper implements Function<Category, CategoryResponse> {

    @Override
    public CategoryResponse apply(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );

    }

}
