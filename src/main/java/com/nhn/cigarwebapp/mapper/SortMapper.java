package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.entity.Order_;
import com.nhn.cigarwebapp.entity.Product_;
import com.nhn.cigarwebapp.specification.sort.OrderSortEnum;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortMapper {

    public Sort getProductSort(String sort) {
        if (sort != null) {
            switch (sort) {
                case ProductSortEnum.DEFAULT -> {
                    return Sort.by(Sort.Order.desc(Product_.UNITS_IN_STOCK)).and(Sort.by(Sort.Order.desc(Product_.CREATED_DATE)));
                }
                case ProductSortEnum.ADMIN_DEFAULT -> {
                    return Sort.by(Sort.Order.desc(Product_.ACTIVE)).and(Sort.by(Sort.Order.desc(Product_.CREATED_DATE)));
                }
                case ProductSortEnum.PRICE_DESC -> {
                    return Sort.by(Sort.Order.desc(Product_.SALE_PRICE));
                }
                case ProductSortEnum.PRICE_ASC -> {
                    return Sort.by(Sort.Order.asc(Product_.SALE_PRICE));
                }
                case ProductSortEnum.NEWEST -> {
                    return Sort.by(Sort.Order.desc(Product_.CREATED_DATE));
                }
            }
        }

        return null;
    }

    public Sort getOrderSort(String sort) {
        if (sort != null) {
            if (sort.equals(OrderSortEnum.ORDER_STATUS_DESC))
                return Sort.by(Sort.Order.desc(Order_.ORDER_STATUS));

            if (sort.equals(OrderSortEnum.ORDER_STATUS_ASC))
                return Sort.by(Sort.Order.asc(Order_.ORDER_STATUS));

            if (sort.equals(OrderSortEnum.CREATED_AT_DESC))
                return Sort.by(Sort.Order.desc(Order_.CREATED_AT));

            if (sort.equals(OrderSortEnum.CREATED_AT_ASC))
                return Sort.by(Sort.Order.asc(Order_.CREATED_AT));
        }

        return null;
    }
}
