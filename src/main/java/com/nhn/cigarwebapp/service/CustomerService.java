package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.CustomerRequest;
import com.nhn.cigarwebapp.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    boolean isEmailExisted(String email);

    boolean isPhoneNumberExisted(String phoneNumber);

    List<CustomerResponse> getCustomers();

    void addCustomers(List<CustomerRequest> requestList);

}
