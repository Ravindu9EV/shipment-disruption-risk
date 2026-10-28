package com.shipmentdisruptionrisk.sdrdemo.config;

import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleStatus;
import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleType;
import com.shipmentdisruptionrisk.sdrdemo.entity.Rule;
import com.shipmentdisruptionrisk.sdrdemo.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RuleSeeder implements CommandLineRunner {

    private final RuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ruleRepository.count() == 0) {
            log.info("Seeding risk rules...");
            List<Rule> rules = List.of(
                Rule.builder().name("Severe Weather").ruleType(RuleType.WEATHER).points(30).status(RuleStatus.ACTIVE).build(),
                Rule.builder().name("Low Carrier Reliability").ruleType(RuleType.CARRIER_RELIABILITY).points(25).status(RuleStatus.ACTIVE).build(),
                Rule.builder().name("Long Distance").ruleType(RuleType.DISTANCE).points(15).status(RuleStatus.ACTIVE).build(),
                Rule.builder().name("Extended Lead Time").ruleType(RuleType.LEAD_TIME).points(20).status(RuleStatus.ACTIVE).build(),
                Rule.builder().name("Geopolitical Risk").ruleType(RuleType.GEOPOLITICAL).points(20).status(RuleStatus.ACTIVE).build()
            );
            ruleRepository.saveAll(rules);
            log.info("Successfully seeded {} rules.", rules.size());
        }
    }
}
