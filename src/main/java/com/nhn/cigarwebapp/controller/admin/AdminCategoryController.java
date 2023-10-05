package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.CategoryRequest;
import com.nhn.cigarwebapp.dto.response.CategoryResponse;
import com.nhn.cigarwebapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCategories(@RequestParam Map<String, String> params) {
        Page<CategoryResponse> categoryList = categoryService.getAdminCategories(params);

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
