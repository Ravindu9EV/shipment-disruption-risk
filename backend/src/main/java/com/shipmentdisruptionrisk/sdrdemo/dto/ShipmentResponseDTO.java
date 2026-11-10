package com.shipmentdisruptionrisk.sdrdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDTO {
//    private Long id;
//    private String shipmentId;
//    private String originPort;
//    private String destination;
//    private Double distance;
//    private String riskLevel;
//    private Double riskScore;
//    private Double geopoliticalRisk;
//    private String weatherCondition;

    private Long id;
    private String shipmentId;
    private LocalDate shipmentDate;
    private String originPort;
    private String destination;
    private String transportMode;
    private String productCategory;
    private Double distance;
    private Double weight;
    private Double fuelPrice;
    private Double carrierReliability;
    private Double leadTime;
    private Boolean disruptionOccurred;

    private Double geopoliticalRisk;
    private String weatherCondition;

    private String riskLevel;
    private Double riskScore;
    private LocalDateTime assessedAt;
}
