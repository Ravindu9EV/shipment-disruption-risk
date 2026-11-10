package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.Utility.RiskLevel;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {

    Optional<Shipment> findShipmentByShipmentId(String shipmentId);
    @EntityGraph(attributePaths = "riskAssessment")
    Page<Shipment> findAll(Pageable pageable);
    @EntityGraph(attributePaths = "riskAssessment")
    Page<Shipment> findByRiskAssessment_RiskLevel(RiskLevel riskLevel,Pageable pageable);
}
