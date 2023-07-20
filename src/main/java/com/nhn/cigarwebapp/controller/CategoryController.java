package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.mapper.request.CategoryRequestMapper;
import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.request.category.CategoryRequest;
import com.nhn.cigarwebapp.model.response.category.CategoryResponse;
import com.nhn.cigarwebapp.service.CategoryService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRequestMapper categoryRequestMapper;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCategories() {

        List<CategoryResponse> categoryList = categoryService.getCategories();

        if (!categoryList.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Categories founds")
                            .result(categoryList)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(List.of())
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertCategories(@RequestBody CategoryRequest request) {
        try {
            categoryService.addCategory(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your category have been saved")
                            .result("")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your category")
                            .result(e.getMessage())
                            .build());
        }
    }

}
