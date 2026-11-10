package com.shipmentdisruptionrisk.sdrdemo.service.impl;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.RiskAssessment;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import com.shipmentdisruptionrisk.sdrdemo.repository.ShipmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;




    @Override
    public Page<ShipmentResponseDTO> getAllShipments(Pageable pageable) {
        Page<Shipment> shipments=shipmentRepository.findAll(pageable);
        return shipments.map(shipment -> {
            RiskAssessment risk = shipment.getRiskAssessment();
            return ShipmentResponseDTO.builder()
                .id(shipment.getId())
                .shipmentId(shipment.getShipmentId())
                .originPort(shipment.getOriginPort())
                .destination(shipment.getDestination())
                .distance(shipment.getDistance())
                .riskLevel(risk != null ? risk.getRiskLevel().name() : "PENDING")
                .riskScore(risk!= null ? risk.getRiskScore() : 0.0)
                .geopoliticalRisk(shipment.getGeopoliticalRisk())
                .weatherCondition(shipment.getWeatherCondition())
                .build();
        });
    }





    @Override
    public Optional<Shipment> getShipmentById(Long id) {
        return shipmentRepository.findById(id);
    }

    @Override
    public Optional<Shipment> getShipmentByShipmentId(String shipmentId) {
        return shipmentRepository.findShipmentByShipmentId(shipmentId);
    }

    @Override
    public ShipmentResponseDTO toDto(Shipment shipment) {
        RiskAssessment risk = shipment.getRiskAssessment();
        String level = (risk != null && risk.getRiskLevel() !=null) ? risk.getRiskLevel().name() : "PENDING";

        return ShipmentResponseDTO.builder()
//            .id(shipment.getId())
//            .shipmentId(shipment.getShipmentId())
//            .originPort(shipment.getOriginPort())
//            .destination(shipment.getDestination())
//            .distance(shipment.getDistance())
//            .riskLevel(level)
//            .riskScore(risk !=null ? risk.getRiskScore() : 0.0)
//            .geopoliticalRisk(shipment.getGeopoliticalRisk())
//            .weatherCondition(shipment.getWeatherCondition())
//            .build();
            .id(shipment.getId())
            .shipmentId(shipment.getShipmentId())
            .shipmentDate(shipment.getShipmentDate())
            .originPort(shipment.getOriginPort())
            .destination(shipment.getDestination())
            .transportMode(shipment.getTransportMode())
            .productCategory(shipment.getProductCategory())
            .distance(shipment.getDistance())
            .weight(shipment.getWeight())
            .fuelPrice(shipment.getFuelPrice())
            .carrierReliability(shipment.getCarrierReliability())
            .leadTime(shipment.getLeadTime())
            .disruptionOccurred(shipment.getDisruptionOccurred())
            .geopoliticalRisk(shipment.getGeopoliticalRisk())
            .weatherCondition(shipment.getWeatherCondition())
            .riskLevel(risk != null && risk.getRiskLevel() != null ? risk.getRiskLevel().name() : "PENDING")
            .riskScore(risk != null ? risk.getRiskScore() : null)
            .assessedAt(risk != null ? risk.getAssessedAt() : null)
            .build();
    }
}
