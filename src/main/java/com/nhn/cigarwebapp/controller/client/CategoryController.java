package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> categoryDetail(@PathVariable(name = "id") String id) {
        CategoryResponse categoryResponse = categoryService.getCategoryDetail(Long.valueOf(id));

        if (categoryResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Category found with id = " + id)
                            .result(categoryResponse)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());

    }

}
