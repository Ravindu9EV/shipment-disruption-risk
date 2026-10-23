package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    Optional<Shipment> findByShipmentId(String shipmentId);
}
