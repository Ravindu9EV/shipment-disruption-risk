package com.shipmentdisruptionrisk.sdrdemo.dto;

import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ShipmentDTO {
    private Long id;
    private Date shipmentDate;
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
}
