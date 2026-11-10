package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.repository.ShipmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.RiskEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskEngineController {

    private final RiskEngineService riskEngineService;
    private final ShipmentRepository shipmentRepository;
    @PostMapping("/assess-all")
    public ResponseEntity<String> triggerRiskAssessment() {
        riskEngineService.assessAllShipments();
        return ResponseEntity.ok("Risk assessment process completed.");
    }

    @GetMapping("/shipments/{id}/breakdown")
    public ResponseEntity<?> breakdwon(@PathVariable("id") Long id){
        return riskEngineService.breakdwon(id);
    }
}

