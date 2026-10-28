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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEngineServiceImpl implements RiskEngineService {

    private final ShipmentRepository shipmentRepository;
    private final RuleRepository ruleRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    @Override
    public void assessAllShipments() {
        log.info("Starting Risk Engine...");

        //get all active rules
        List<Rule> activeRules = ruleRepository.findByStatus(RuleStatus.ACTIVE);

        //get all shipments
        List<Shipment> shipments = shipmentRepository.findAll();

        int successCount=0;

        for(Shipment shipment : shipments){
            //skip if already assessed
            if(riskAssessmentRepository.findByShipment(shipment).isPresent()){
                continue;
            }
            double totalScore = 0.0;

            // Evaluate each rule against the shipment
            for(Rule rule : activeRules){
                if(rule.getRuleType() == RuleType.CARRIER_RELIABILITY){
                    // if carrier reliability is less than o.6 add points
                    if(shipment.getCarrierReliability()<0.6){
                        totalScore+= rule.getPoints();
                    }
                } else if (rule.getRuleType() == RuleType.DISTANCE) {
                    //if distance is greater than 10,000 km, add points
                    if(shipment.getDistance()>10000){
                        totalScore+= rule.getPoints();
                    }

                } else if (rule.getRuleType() == RuleType.LEAD_TIME) {
                    //if lead time is greater than 30 days, add points
                    if(shipment.getLeadTime() > 30){
                        totalScore+=rule.getPoints();
                    }
                }
                //weather rule

                //Geopolitical rule
            }
            RiskLevel riskLevel;
            if(totalScore >=60){
                riskLevel=RiskLevel.HIGH;
            } else if (totalScore >=30) {
                riskLevel =RiskLevel.MEDIUM;
            }else {
                riskLevel = RiskLevel.LOW;
            }

            RiskAssessment assessment = RiskAssessment.builder()
                .shipment(shipment)
                .riskScore(totalScore)
                .riskLevel(riskLevel)
                .assessedAt(LocalDateTime.now())
                .build();

            riskAssessmentRepository.save(assessment);
            successCount++;
        }
        log.info("Risk Assessment complete. Assessed {} shipment.", successCount);
    }
}
