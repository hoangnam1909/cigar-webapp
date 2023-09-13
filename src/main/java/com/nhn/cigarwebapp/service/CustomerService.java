package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.CustomerRequest;
import com.nhn.cigarwebapp.dto.response.CustomerResponse;
import com.nhn.cigarwebapp.model.Customer;

import java.util.List;

public interface CustomerService {

    boolean isEmailExisted(String email);

    boolean isPhoneNumberExisted(String phoneNumber);

    List<CustomerResponse> getCustomers();

    void addCustomer(Customer customer);

    void addCustomer(CustomerRequest request);

    void addCustomers(List<CustomerRequest> requestList);

}
