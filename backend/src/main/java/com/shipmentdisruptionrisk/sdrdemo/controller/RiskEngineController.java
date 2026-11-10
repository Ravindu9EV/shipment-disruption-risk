package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.entity.RiskAssessment;
import com.shipmentdisruptionrisk.sdrdemo.repository.RiskAssessmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.repository.ShipmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.RiskEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
@Tag(name = "Risk Engine", description = "Endpoints for running risk assessments and viewing summaries")
public class RiskEngineController {

    private final RiskEngineService riskEngineService;
    private final RiskAssessmentRepository riskAssessmentRepository;
    @PostMapping("/assess-all")
    @Operation(summary = "Trigger risk assessment", description = "Evaluates all shipments against active rules and saves the results.")
    public ResponseEntity<String> triggerRiskAssessment() {
        riskEngineService.assessAllShipments();
        return ResponseEntity.ok("Risk assessment process completed.");
    }

    @GetMapping("/shipments/{id}/breakdown")
    public ResponseEntity<?> breakdown(@PathVariable("id") Long id){
        return riskEngineService.breakdown(id);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns total counts of HIGH, MEDIUM, and LOW risk shipments, plus the average score.")
    public Map<String, Object> summary(){
        List<RiskAssessment> all = riskAssessmentRepository.findAll();
        Map<String, Long> byLevel = all.stream().collect(Collectors.groupingBy(a->a.getRiskLevel().name(), Collectors.counting()));
        double avg=all.stream().mapToDouble(RiskAssessment::getRiskScore).average().orElse(0);
        return Map.of("total", all.size(), "byLevel", byLevel, "averageScore", Math.round(avg * 10)/10.0);
    }
}

