package com.nhn.cigarwebapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "delivery_company_id", referencedColumnName = "id")
    private DeliveryCompany deliveryCompany;

}
