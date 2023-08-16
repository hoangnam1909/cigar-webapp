package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.common.ProductEnum;
import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpecificationConverter {

    public ProductSpecification productSpecification(Map<String, String> params) {
        ProductSpecification specification = new ProductSpecification();

        if (params.containsKey(ProductEnum.CATEGORY_ID)) {
            specification.add(new SearchCriteria(ProductEnum.CATEGORY_ID, params.get(ProductEnum.CATEGORY_ID), SearchOperation.CATEGORY_ID));
        }

        if (params.containsKey(ProductEnum.BRAND_ID)) {
            specification.add(new SearchCriteria(ProductEnum.BRAND_ID, params.get(ProductEnum.BRAND_ID), SearchOperation.BRAND_ID));
        }

        if (params.containsKey(ProductEnum.NAME)) {
            specification.add(new SearchCriteria(ProductEnum.NAME, params.get(ProductEnum.NAME), SearchOperation.MATCH));
        }

        return specification;
    }

}
