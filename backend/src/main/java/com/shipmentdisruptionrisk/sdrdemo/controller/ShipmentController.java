package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import com.shipmentdisruptionrisk.sdrdemo.service.impl.ShipmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "Endpoints for viewing shipment records and their assigned risks")
public class ShipmentController {



    private final ShipmentServiceImpl service;


    @GetMapping
    @Operation(summary = "Get paginated shipments", description = "Returns a paginated list of shipments, optionally filtered by risk level (HIGH, MEDIUM, LOW).")
    public ResponseEntity<Page<ShipmentResponseDTO>> getAllShipments(@RequestParam(name =  "page",defaultValue="0") int page, @RequestParam(name = "size",defaultValue = "20")int size, @RequestParam(name = "riskLevel", required = false) String riskLevel) {
        Pageable pageable=  PageRequest.of(page,size);
        return ResponseEntity.ok(service.getAllShipments(pageable, riskLevel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shipment by ID", description = "Returns full details for a single shipment, including its current risk score.")
    public ResponseEntity<ShipmentResponseDTO> getShipmentById(@PathVariable("id") Long id){
        return service.getShipmentById(id)
            .map(service::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking/{shipmentId}")
    @Operation(summary = "Get shipment by Tracking ID", description = "Finds a shipment using its string Tracking ID (e.g. SC-10000).")
    public ResponseEntity<ShipmentResponseDTO> getShipmentByTrackingId(@PathVariable("shipmentId") String shipmentId){
        return service.getShipmentByShipmentId(shipmentId)
            .map(service::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
