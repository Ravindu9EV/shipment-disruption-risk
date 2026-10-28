package com.shipmentdisruptionrisk.sdrdemo.service;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentService {



    Page<ShipmentResponseDTO> getAllShipments(Pageable pageable);

    List<Shipment> getAllShipments();
    Optional<Shipment> getShipmentById(Long Id);
    Optional<Shipment> getShipmentByShipmentId(String shipmentId);
}
