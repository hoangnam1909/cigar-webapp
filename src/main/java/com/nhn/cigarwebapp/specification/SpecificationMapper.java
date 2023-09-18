package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.specification.brand.BrandEnum;
import com.nhn.cigarwebapp.specification.brand.BrandSpecification;
import com.nhn.cigarwebapp.specification.category.CategoryEnum;
import com.nhn.cigarwebapp.specification.category.CategorySpecification;
import com.nhn.cigarwebapp.specification.order.OrderEnum;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import com.nhn.cigarwebapp.specification.product.ProductEnum;
import com.nhn.cigarwebapp.specification.product.ProductSpecification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpecificationMapper {

    public BrandSpecification brandSpecification(Map<String, String> params) {
        BrandSpecification specification = new BrandSpecification();

        if (params.containsKey(BrandEnum.KEYWORD)) {
            specification.add(new SearchCriteria(BrandEnum.KEYWORD, params.get(BrandEnum.KEYWORD), SearchOperation.ID_NAME));
        }

        if (params.containsKey(BrandEnum.ID)) {
            specification.add(new SearchCriteria(BrandEnum.ID, params.get(BrandEnum.ID), SearchOperation.MATCH));
        }

        if (params.containsKey(BrandEnum.NAME)) {
            specification.add(new SearchCriteria(BrandEnum.NAME, params.get(BrandEnum.NAME), SearchOperation.MATCH));
        }

        return specification;
    }

    public CategorySpecification categorySpecification(Map<String, String> params) {
        CategorySpecification specification = new CategorySpecification();

        if (params.containsKey(CategoryEnum.KEYWORD)) {
            specification.add(new SearchCriteria(CategoryEnum.KEYWORD, params.get(CategoryEnum.KEYWORD), SearchOperation.ID_NAME));
        }

        if (params.containsKey(CategoryEnum.ID)) {
            specification.add(new SearchCriteria(CategoryEnum.ID, params.get(CategoryEnum.ID), SearchOperation.MATCH));
        }

        if (params.containsKey(CategoryEnum.NAME)) {
            specification.add(new SearchCriteria(CategoryEnum.NAME, params.get(CategoryEnum.NAME), SearchOperation.MATCH));
        }

        return specification;
    }

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

        if (params.containsKey(ProductEnum.KEYWORD)) {
            specification.add(new SearchCriteria(ProductEnum.KEYWORD, params.get(ProductEnum.KEYWORD), SearchOperation.ID_NAME));
        }

        return specification;
    }

    public OrderSpecification orderSpecification(Map<String, String> params) {
        OrderSpecification specification = new OrderSpecification();

        if (params.containsKey(OrderEnum.ORDER_STATUS_ID)) {
            specification.add(new SearchCriteria(OrderEnum.ORDER_STATUS_ID, params.get(OrderEnum.ORDER_STATUS_ID), SearchOperation.ORDER_STATUS_ID));
        }

        if (params.containsKey(OrderEnum.DELIVERY_COMPANY_ID)) {
            specification.add(new SearchCriteria(OrderEnum.DELIVERY_COMPANY_ID, params.get(OrderEnum.DELIVERY_COMPANY_ID), SearchOperation.DELIVERY_COMPANY_ID));
        }

        if (params.containsKey(OrderEnum.KEYWORD)) {
            specification.add(new SearchCriteria(OrderEnum.KEYWORD, params.get(OrderEnum.KEYWORD), SearchOperation.ID_NAME));
        }

        return specification;
    }

}
