package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    @Autowired
    private ProductRepository productRepository;

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.name())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        Long productsCount = productRepository.countProductByCategory(category);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                productsCount
        );
    }

}
