package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.model.common.SearchCriteria;
import com.nhn.cigarwebapp.model.common.SearchOperation;
import com.nhn.cigarwebapp.model.entity.Brand;
import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.entity.Product;
import jakarta.persistence.criteria.*;
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
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
