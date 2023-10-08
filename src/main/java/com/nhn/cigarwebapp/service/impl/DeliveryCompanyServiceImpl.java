package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.response.admin.DeliveryCompanyAdminResponse;
import com.nhn.cigarwebapp.mapper.DeliveryCompanyMapper;
import com.nhn.cigarwebapp.repository.DeliveryCompanyRepository;
import com.nhn.cigarwebapp.service.DeliveryCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyServiceImpl implements DeliveryCompanyService {

    private final DeliveryCompanyRepository deliveryCompanyRepository;
    private final DeliveryCompanyMapper deliveryCompanyMapper;

    @Override
    @Cacheable("List<DeliveryCompanyAdminResponse>")
    public List<DeliveryCompanyAdminResponse> getDeliveryCompanies() {
        return deliveryCompanyRepository.findAll()
                .stream()
                .map(deliveryCompanyMapper::toResponse)
                .toList();
    }

}
