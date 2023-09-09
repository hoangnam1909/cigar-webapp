package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    BrandResponse getBrand(Long id);

    Page<ProductResponse> getProductOfBrand(Long id, int page, int size);

    List<BrandResponse> getBrands();

    List<BrandAdminResponse> getAdminBrands();

    List<BrandWithProductsResponse> getTop(int top);

    void addBrand(BrandCreationRequest request);

    BrandResponse update(Long id, BrandUpdateRequest request);

}
