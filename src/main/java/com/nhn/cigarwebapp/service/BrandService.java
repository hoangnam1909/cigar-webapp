package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.BrandRequest;
import com.nhn.cigarwebapp.dto.request.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {

    BrandDetailResponse getBrandDetail(Long id);

    Page<ProductResponse> getProductOfBrand(Long id, int page, int size);

    List<BrandResponse> getBrands();

    List<BrandWithProductsResponse> getTop3();

    void addBrand(BrandRequest request);

    BrandResponse update(Long id, BrandUpdateRequest request);

}
