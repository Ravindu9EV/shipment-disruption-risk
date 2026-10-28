package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.entity.RiskAssessment;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    Optional<RiskAssessment> findByShipment(Shipment shipment);
}
