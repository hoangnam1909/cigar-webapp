package com.nhn.cigarwebapp.specification.product;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.model.Brand;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.Product;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    private List<SearchCriteria> criteriaList;

    public ProductSpecification() {
        this.criteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation().equals(SearchOperation.CATEGORY_ID)) {
                Join<Category, Product> categoryProductJoin = root.join("category");
                predicates.add(builder.equal(categoryProductJoin.get("id"), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.BRAND_ID)) {
                Join<Brand, Product> brandProductJoin = root.join("brand");
                predicates.add(builder.equal(brandProductJoin.get("id"), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey()).as(String.class)),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase()) + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.IS_ACTIVE)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()).as(Boolean.class), criteria.getValue()));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
