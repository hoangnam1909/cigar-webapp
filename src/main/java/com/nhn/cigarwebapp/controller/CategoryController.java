package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.request.category.CategoryCreationRequest;
import com.nhn.cigarwebapp.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ResponseObject> getCategories(){

        List<Category> categoryList = categoryRepository.findAll();

        if (!categoryList.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Categories founds")
                            .data(categoryList)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Categories not found")
                            .data(List.of())
                            .build());

    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertCategories(@RequestBody List<CategoryCreationRequest> request) {
        try {
            List<Category> categories = request.stream()
                    .map(c -> {
                        Category cate = modelMapper.map(c, Category.class);

                        if (c.getParentCategoryId() != null) {
                            Optional<Category> parentCategory = categoryRepository.findById(c.getParentCategoryId());
                            if (parentCategory.isPresent())
                                cate.setParentCategory(parentCategory.get());
                            else
                                cate.setParentCategory(null);
                        }

                        return cate;
                    })
                    .collect(Collectors.toList());
            categoryRepository.saveAll(categories);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your categories have been saved.")
                            .data(categories)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your categories.")
                            .data(e.getMessage())
                            .build());
        }
    }

}
