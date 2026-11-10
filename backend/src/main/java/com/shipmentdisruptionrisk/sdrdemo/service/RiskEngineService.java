package com.shipmentdisruptionrisk.sdrdemo.service;

import org.springframework.http.ResponseEntity;

public interface RiskEngineService {
    void assessAllShipments();
    ResponseEntity<?> breakdown(Long id);
}
