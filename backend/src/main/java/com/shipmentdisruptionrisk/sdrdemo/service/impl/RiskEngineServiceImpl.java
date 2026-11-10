package com.shipmentdisruptionrisk.sdrdemo.service.impl;

import com.shipmentdisruptionrisk.sdrdemo.Utility.RiskLevel;
import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleStatus;
import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleType;
import com.shipmentdisruptionrisk.sdrdemo.entity.RiskAssessment;
import com.shipmentdisruptionrisk.sdrdemo.entity.Rule;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import com.shipmentdisruptionrisk.sdrdemo.repository.RiskAssessmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.repository.RuleRepository;
import com.shipmentdisruptionrisk.sdrdemo.repository.ShipmentRepository;
import com.shipmentdisruptionrisk.sdrdemo.service.RiskEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEngineServiceImpl implements RiskEngineService {

    private final ShipmentRepository shipmentRepository;
    private final RuleRepository ruleRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

   //thresholds
    private static final Set<String> SERVER_WEATHER = Set.of("Hurricane", "Storm", "Typhoon", "Cyclone");
    private static final double MIN_CARRIER_RELIABILITY= 0.6;
    private static final double LONG_DISTANCE_KM =  10000;
    private static final double EXTENDED_LEAD_TIME_DAYS = 30;
    private static final double HIGH_GEOPOLITICAL_RISK = 7.0;

    @Override
    @Transactional
    public void assessAllShipments() {
        log.info("Starting Risk Engine...");

        //get all active rules
        List<Rule> activeRules = ruleRepository.findByStatus(RuleStatus.ACTIVE);

        //get all shipments
        List<Shipment> shipments = shipmentRepository.findAll();

        int created=0;
        int updated=0;
        //int successCount=0;

        for(Shipment shipment : shipments){

            double totalScore = 0.0;

            for(Rule rule: activeRules){
                if(eveluateRule(rule,shipment)){
                    totalScore+= rule.getPoints();
                }
            }


            RiskLevel riskLevel;
            if(totalScore >=60){
                riskLevel=RiskLevel.HIGH;
            } else if (totalScore >=30) {
                riskLevel =RiskLevel.MEDIUM;
            }else {
                riskLevel = RiskLevel.LOW;
            }

            RiskAssessment assessment = riskAssessmentRepository.findByShipment(shipment)
                .orElseGet(()-> {
                    RiskAssessment a = RiskAssessment.builder().shipment(shipment).build();
                    return a;
                });

            boolean isNew = assessment.getId() == null;
            assessment.setRiskScore(totalScore);
            assessment.setRiskLevel(riskLevel);
            assessment.setAssessedAt(LocalDateTime.now());
            riskAssessmentRepository.save(assessment);

            if(isNew) created++;else updated++;


            riskAssessmentRepository.save(assessment);

        }
        log.info("Risk Assessment complete. Created: {}, Updated: {} ", created,updated);
    }



    @Override
    public ResponseEntity<?> breakdwon(Long id) {
        return shipmentRepository.findById(id).map(
            s-> {
                List<Rule> activeRules = ruleRepository.findByStatus(RuleStatus.ACTIVE);
                List<Map<String, Object>> triggered = activeRules.stream()
                    .filter(r -> eveluateRule(r, s))
                    .map(r -> Map.<String, Object>of(
                        "name", r.getName(), "points", r.getPoints(), "type", r.getRuleType().name()
                    )).collect(Collectors.toList());

                double totalScore = triggered.stream()
                    .mapToDouble(m -> ((Integer) m.get("points")).doubleValue())
                    .sum();
                return ResponseEntity.ok(Map.of(
                    "shipmentId", s.getShipmentId(),
                    "totalScore", totalScore,
                    "triggeredRules", triggered
                ));
            })
                .orElse(ResponseEntity.notFound().build());

    }

    private boolean eveluateRule(Rule rule, Shipment shipment) {
        if(rule.getRuleType() == null) return false;
        return switch (rule.getRuleType()){
            case WEATHER -> shipment.getWeatherCondition() !=null && SERVER_WEATHER.contains(shipment.getWeatherCondition().trim());
            case CARRIER_RELIABILITY -> shipment.getCarrierReliability() < MIN_CARRIER_RELIABILITY;
            case DISTANCE -> shipment.getDistance() > LONG_DISTANCE_KM;
            case LEAD_TIME -> shipment.getLeadTime() > EXTENDED_LEAD_TIME_DAYS;
            case GEOPOLITICAL -> shipment.getGeopoliticalRisk() != null && shipment.getGeopoliticalRisk() >=HIGH_GEOPOLITICAL_RISK;
        };
    }
}
