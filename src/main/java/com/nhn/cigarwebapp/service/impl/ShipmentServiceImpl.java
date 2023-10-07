package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.entity.Shipment;
import com.nhn.cigarwebapp.repository.ShipmentRepository;
import com.nhn.cigarwebapp.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public Shipment add(String address) {
        return shipmentRepository.save(Shipment.builder()
                .address(address)
                .build());
    }

}
