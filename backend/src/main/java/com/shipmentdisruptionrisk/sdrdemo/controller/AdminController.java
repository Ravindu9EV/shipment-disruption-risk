package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.service.ShipmentIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ShipmentIngestionService ingestionService;

    @PostMapping("/ingest")
    public ResponseEntity<String> triggerIngestion() {
        ingestionService.ingestShipments();
        return ResponseEntity.ok("Shipment ingestion process triggered successfully.");
    }
}
