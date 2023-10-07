package com.nhn.cigarwebapp.specification.payment_destination;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.entity.PaymentDestination;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentDestinationSpecification implements Specification<PaymentDestination> {

    private List<SearchCriteria> criteriaList;

    public PaymentDestinationSpecification() {
        this.criteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<PaymentDestination> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation().equals(SearchOperation.IS_ACTIVE)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()).as(Boolean.class), Boolean.parseBoolean((String) criteria.getValue())));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
