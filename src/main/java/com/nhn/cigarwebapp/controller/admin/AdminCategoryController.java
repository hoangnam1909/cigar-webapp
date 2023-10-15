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
        try {
            categoryService.addCategory(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your category have been saved")
                            .result("")
                            .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long id,
                                                         @RequestBody CategoryRequest request) {
        try {
            CategoryResponse response = categoryService.updateCategory(id, request);

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
                                .result(null)
                                .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

    @DeleteMapping("/{id}")
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
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

}
