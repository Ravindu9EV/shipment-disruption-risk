package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShipmentRepository {
    Optional<Shipment> findById(Long id);
    Optional<Shipment> findByShipmentDate(String shipmentDate);
}
