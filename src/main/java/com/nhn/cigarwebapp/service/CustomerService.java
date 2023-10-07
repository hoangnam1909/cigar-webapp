package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.customer.CustomerRequest;
import com.nhn.cigarwebapp.dto.response.customer.CustomerResponse;
import com.nhn.cigarwebapp.entity.Customer;

import java.util.List;

public interface CustomerService {

    boolean isEmailExisted(String email);

    boolean isPhoneNumberExisted(String phoneNumber);

    List<CustomerResponse> getCustomers();

    void addCustomer(Customer customer);

    void addCustomer(CustomerRequest request);

    void addCustomers(List<CustomerRequest> requestList);

}
