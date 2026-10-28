package com.shipmentdisruptionrisk.sdrdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDTO {
    private Long id;
    private String shipmentId;
    private String originPort;
    private String destination;
    private Double distance;
    private String riskLevel;
    private Double riskScore;
}
