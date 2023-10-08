package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.dto.response.payment.PaymentDestinationResponse;
import com.nhn.cigarwebapp.mapper.PaymentDestinationMapper;
import com.nhn.cigarwebapp.repository.PaymentDestinationRepository;
import com.nhn.cigarwebapp.service.PaymentDestinationService;
import com.nhn.cigarwebapp.specification.payment_destination.PaymentDestinationEnum;
import com.nhn.cigarwebapp.specification.payment_destination.PaymentDestinationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentDestinationServiceImpl implements PaymentDestinationService {

    private final PaymentDestinationRepository paymentDestinationRepository;
    private final PaymentDestinationMapper paymentDestinationMapper;

    @Override
    @Cacheable("List<PaymentDestinationResponse>")
    public List<PaymentDestinationResponse> getPaymentDestinations() {
        PaymentDestinationSpecification specification = new PaymentDestinationSpecification();
        specification.add(new SearchCriteria(PaymentDestinationEnum.IS_ACTIVE, "true", SearchOperation.IS_ACTIVE));

        return paymentDestinationRepository
                .findAll(specification, Sort.by(Sort.Order.asc("sortIndex")))
                .stream()
                .map(paymentDestinationMapper::toResponse)
                .toList();
    }

}
