package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.BrandRequest;
import com.nhn.cigarwebapp.dto.request.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandDetailResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;

import java.util.List;

public interface BrandService {

    BrandDetailResponse getBrandDetail(Long id);

    List<ProductResponse> getProductOfBrand(Long id);

    List<BrandResponse> getBrands();

    void addBrand(BrandRequest request);

    BrandResponse update(Long id, BrandUpdateRequest request);

}
