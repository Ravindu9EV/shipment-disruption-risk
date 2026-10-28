package com.shipmentdisruptionrisk.sdrdemo.entity;

import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleStatus;
import com.shipmentdisruptionrisk.sdrdemo.Utility.RuleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType ruleType;

    @Column(nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleStatus status;
}
