package com.shipmentdisruptionrisk.sdrdemo.dto;

import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ShipmentDTO {
    private Long id;
    private String shipmentId;
    private LocalDate shipmentDate;
    private String originPort;
    private String destination;
    private String transportMode;
    private String productCategory;
    private double distance;
    private double weight;
    private double fuelPrice;
    private double carrierReliability;
    private double leadTime;
    private Boolean disruptionOccurred;

    @Builder
    public ShipmentDTO(String shipmentId, LocalDate shipmentDate, String originPort,
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
