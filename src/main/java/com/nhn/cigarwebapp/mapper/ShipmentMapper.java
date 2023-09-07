package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.ShipmentResponse;
import com.nhn.cigarwebapp.model.Shipment;
import com.nhn.cigarwebapp.repository.DeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentMapper {

    private final DeliveryCompanyRepository deliveryCompanyRepository;

    public ShipmentResponse toShipmentResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .id(shipment.getId())
                .address(shipment.getAddress())
                .trackingNumber(shipment.getTrackingNumber())
                .deliveryCompany(shipment.getDeliveryCompany().getName())
                .build();
    }

}
