package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

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


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> insertCategory(@RequestBody CategoryRequest request) {
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable(name = "id") String id,
                                                         @RequestBody CategoryRequest request) {
        try {
            CategoryResponse response = categoryService.updateCategory(Long.valueOf(id), request);

            if (response != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your category have been saved")
                                .result("")
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Something went wrong")
                                .result(response)
                                .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your category")
                            .result(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable(name = "id") String id) {
        try {
            boolean isDeleted = categoryService.deleteCategory(Long.valueOf(id));

            if (isDeleted)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your category have been deleted")
                                .result("")
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Something went wrong")
                                .result(null)
                                .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not delete your category")
                            .result(e.getMessage())
                            .build());
        }
    }

}
