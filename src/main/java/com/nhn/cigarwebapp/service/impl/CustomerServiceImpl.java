package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.dto.request.CustomerRequest;
import com.nhn.cigarwebapp.dto.response.CustomerResponse;
import com.nhn.cigarwebapp.mapper.CustomerMapper;
import com.nhn.cigarwebapp.repository.CustomerRepository;
import com.nhn.cigarwebapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public boolean isEmailExisted(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberExisted(String phoneNumber) {
        return customerRepository.existsByPhone(phoneNumber);
    }

    @Override
    public List<CustomerResponse> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> customerMapper.toResponse(customer))
                .toList();
    }

    @Override
    @Transactional
    public void addCustomers(List<CustomerRequest> requestList) {
        requestList.stream()
                .map(customerRequest -> customerMapper.toEntity(customerRequest))
                .forEach(customer -> customerRepository.save(customer));
    }

}
