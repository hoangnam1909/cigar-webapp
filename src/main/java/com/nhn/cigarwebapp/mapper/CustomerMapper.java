package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.request.admin.AdminOrderCreationRequest;
import com.nhn.cigarwebapp.dto.request.customer.CustomerRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderRequest;
import com.nhn.cigarwebapp.dto.request.order.OrderWithPaymentRequest;
import com.nhn.cigarwebapp.dto.response.customer.CustomerResponse;
import com.nhn.cigarwebapp.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public Customer toEntity(OrderRequest request){
        return Customer.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public Customer toEntity(OrderWithPaymentRequest request){
        return Customer.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public Customer toEntity(AdminOrderCreationRequest request){
        return Customer.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }

}
