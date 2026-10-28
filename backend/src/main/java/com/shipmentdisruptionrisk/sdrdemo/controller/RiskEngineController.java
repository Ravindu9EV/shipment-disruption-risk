package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.service.RiskEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskEngineController {

    private final RiskEngineService riskEngineService;

    @PostMapping("/assess-all")
    public ResponseEntity<String> triggerRiskAssessment() {
        riskEngineService.assessAllShipments();
        return ResponseEntity.ok("Risk assessment process completed.");
    }
}
