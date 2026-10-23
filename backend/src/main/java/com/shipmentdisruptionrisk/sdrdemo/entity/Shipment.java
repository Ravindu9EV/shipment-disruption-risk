package com.shipmentdisruptionrisk.sdrdemo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String shipmentId;
    @Column(nullable = false)
    private LocalDate shipmentDate;
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


    @Builder
    public Shipment(String shipmentId, LocalDate shipmentDate, String originPort,
                    String destination, String transportMode, String productCategory,
                    double distance, double weight, double fuelPrice,
                    double carrierReliability, double leadTime, Boolean disruptionOccurred) {
        this.shipmentId = shipmentId;
        this.shipmentDate = shipmentDate;
        this.originPort = originPort;
        this.destination = destination;
        this.transportMode = transportMode;
        this.productCategory = productCategory;
        this.distance = distance;
        this.weight = weight;
        this.fuelPrice = fuelPrice;
        this.carrierReliability = carrierReliability;
        this.leadTime = leadTime;
        this.disruptionOccurred = disruptionOccurred;
    }
}
