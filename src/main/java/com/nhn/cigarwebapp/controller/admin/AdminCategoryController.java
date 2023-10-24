package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.category.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.category.CategoryResponse;
import com.nhn.cigarwebapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCategoryController {

    Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCategories(@RequestParam Map<String, String> params) {
        Page<CategoryResponse> categoryList = categoryService.getAdminCategories(params);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Categories founds")
                        .result(categoryList)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertCategory(@RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Your category have been saved")
                        .result("")
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long id,
                                                         @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        if (response != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your category have been saved")
                            .result(null)
                            .build());
        else
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Something went wrong")
                            .result(null)
                            .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = categoryService.deleteCategory(id);
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
    }

}
