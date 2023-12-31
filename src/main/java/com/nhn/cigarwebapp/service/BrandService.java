package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.dto.response.brand.BrandResponse;
import com.nhn.cigarwebapp.dto.response.brand.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BrandService {

    BrandResponse getBrand(Long id);

    Page<ProductResponse> getProductOfBrand(Long id, int page, int size);

    List<BrandResponse> getBrands();

    BrandAdminResponse getAdminBrand(Long id);

    Page<BrandAdminResponse> getAdminBrands(Map<String, String> params);

    List<BrandWithProductsResponse> getTop(int top);

    void addBrand(BrandCreationRequest request);

    BrandResponse update(Long id, BrandUpdateRequest request);

}
