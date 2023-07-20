package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.request.brand.BrandRequest;
import com.nhn.cigarwebapp.model.response.brand.BrandDetailResponse;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import com.nhn.cigarwebapp.model.response.category.ProductResponse;

import java.util.List;

public interface BrandService {

    BrandDetailResponse getBrandDetail(Long id);

    List<ProductResponse> getProductOfBrand(Long id);

    List<BrandResponse> getBrands();

    void addBrand(BrandRequest request);

}
