package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.specification.brand.BrandEnum;
import com.nhn.cigarwebapp.specification.brand.BrandSpecification;
import com.nhn.cigarwebapp.specification.category.CategoryEnum;
import com.nhn.cigarwebapp.specification.category.CategorySpecification;
import com.nhn.cigarwebapp.specification.order.OrderEnum;
import com.nhn.cigarwebapp.specification.order.OrderSpecification;
import com.nhn.cigarwebapp.specification.payment.PaymentEnum;
import com.nhn.cigarwebapp.specification.payment.PaymentSpecification;
import com.nhn.cigarwebapp.specification.payment_destination.PaymentDestinationEnum;
import com.nhn.cigarwebapp.specification.payment_destination.PaymentDestinationSpecification;
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

        if (params.containsKey(ProductEnum.IS_ACTIVE)) {
            specification.add(new SearchCriteria(ProductEnum.IS_ACTIVE, params.get(ProductEnum.IS_ACTIVE), SearchOperation.IS_ACTIVE));
        }

        if (params.containsKey(ProductEnum.KEYWORD)) {
            specification.add(new SearchCriteria(ProductEnum.KEYWORD, params.get(ProductEnum.KEYWORD), SearchOperation.ID_NAME));
        }

        if (params.containsKey(ProductEnum.IN_STOCK)) {
            specification.add(new SearchCriteria(ProductEnum.IN_STOCK, params.get(ProductEnum.IN_STOCK), SearchOperation.IN_STOCK));
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

        if (params.containsKey(OrderEnum.PAYMENT_DESTINATION)) {
            specification.add(new SearchCriteria(OrderEnum.PAYMENT_DESTINATION, params.get(OrderEnum.PAYMENT_DESTINATION), SearchOperation.ORDER_PAYMENT_DESTINATION));
        }

        return specification;
    }

    public PaymentSpecification paymentSpecification(Map<String, String> params) {
        PaymentSpecification specification = new PaymentSpecification();

        if (params.containsKey(PaymentEnum.MOMO_ORDER_ID)) {
            specification.add(new SearchCriteria(PaymentEnum.MOMO_ORDER_ID, params.get(PaymentEnum.MOMO_ORDER_ID), SearchOperation.EQUAL));
        }

        if (params.containsKey(PaymentEnum.MOMO_REQUEST_ID)) {
            specification.add(new SearchCriteria(PaymentEnum.MOMO_REQUEST_ID, params.get(PaymentEnum.MOMO_REQUEST_ID), SearchOperation.EQUAL));
        }

        return specification;
    }

    public PaymentDestinationSpecification paymentDestinationSpecification(Map<String, String> params) {
        PaymentDestinationSpecification specification = new PaymentDestinationSpecification();

        if (params.containsKey(PaymentDestinationEnum.IS_ACTIVE)) {
            specification.add(new SearchCriteria(PaymentDestinationEnum.IS_ACTIVE, params.get(PaymentDestinationEnum.IS_ACTIVE), SearchOperation.IS_ACTIVE));
        }

        return specification;
    }

}
