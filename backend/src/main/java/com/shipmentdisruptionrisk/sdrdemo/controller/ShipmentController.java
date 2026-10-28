package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import com.shipmentdisruptionrisk.sdrdemo.service.impl.ShipmentServiceImpl;
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
public class ShipmentController {



    private final ShipmentServiceImpl service;


    @GetMapping
    public ResponseEntity<Page<ShipmentResponseDTO>> getAllShipments(@RequestParam(name =  "page",defaultValue="0") int page, @RequestParam(name = "size",defaultValue = "20")int size) {
        Pageable pageable= (Pageable) PageRequest.of(page,size);
        return ResponseEntity.ok(service.getAllShipments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Long id){
        return service.getShipmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tracking/{shipmentId}")
    public ResponseEntity<Shipment> getShipmentByTrackingId(@PathVariable String shipmentId){
        return service.getShipmentByShipmentId(shipmentId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
