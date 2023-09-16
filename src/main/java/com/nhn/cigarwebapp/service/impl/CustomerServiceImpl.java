package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.CustomerRequest;
import com.nhn.cigarwebapp.dto.response.CustomerResponse;
import com.nhn.cigarwebapp.mapper.CustomerMapper;
import com.nhn.cigarwebapp.model.Customer;
import com.nhn.cigarwebapp.repository.CustomerRepository;
import com.nhn.cigarwebapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public boolean isEmailExisted(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberExisted(String phoneNumber) {
        return customerRepository.existsByPhone(phoneNumber);
    }

    @Override
    @Cacheable(value = "customers")
    public List<CustomerResponse> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "customers", allEntries = true)
    public void addCustomer(Customer customer) {
        try {
            customerMapper.toResponse(customerRepository.saveAndFlush(customer));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "customers", allEntries = true)
    public void addCustomer(CustomerRequest request) {
        try {
            Customer customer = customerMapper.toEntity(request);
            customerMapper.toResponse(customerRepository.saveAndFlush(customer));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "customers", allEntries = true)
    public void addCustomers(List<CustomerRequest> requestList) {
        requestList.stream()
                .map(customerMapper::toEntity)
                .forEach(customerRepository::save);
    }

}
