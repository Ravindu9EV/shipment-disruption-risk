package com.shipmentdisruptionrisk.sdrdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Shipment {
    @Id
    @Column(unique = true,nullable = false)
    private Long id;
    @Column(nullable = false)
    private Date shipmentDate;
    @Column(nullable = false)
    private String originPort;
    @Column(nullable = false)
    private String destination;
    @Column(nullable = false)
    private String transportMode;
    @Column(nullable = false)
    private String productCategory;
    @Column(nullable = false)
    private double distance;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private double fuelPrice;
    @Column(nullable = false)
    private double carrierReliability;
    @Column(nullable = false)
    private double leadTime;
    @Column(nullable = false)
    private Boolean disruptionOccurred;


}
