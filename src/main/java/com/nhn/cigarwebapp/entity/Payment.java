package com.nhn.cigarwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column
    private Long paidAmount;

    @Column
    private Boolean isPaid;

    @Column
    @JsonIgnore
    private String referenceId;

    @Column
    @JsonIgnore
    private String paymentOrderId;

    @Column(columnDefinition = "TEXT")
    private String paymentUrl;

    @ManyToOne
    @JoinColumn(name = "payment_destination_id", referencedColumnName = "id")
    private PaymentDestination paymentDestination;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private Order order;

}
