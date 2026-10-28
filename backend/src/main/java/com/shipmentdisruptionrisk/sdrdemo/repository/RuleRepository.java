package com.shipmentdisruptionrisk.sdrdemo.repository;

import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleStatus;
import com.shipmentdisruptionrisk.sdrdemo.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByStatus(RuleStatus status);
}
