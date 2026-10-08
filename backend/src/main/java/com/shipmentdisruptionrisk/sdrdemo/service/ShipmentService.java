package com.shipmentdisruptionrisk.sdrdemo.service;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service

public class ShipmentService {
    private static final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    public void add(ShipmentDTO data){
        log.info("Service: add Shipment-->"+data);
    }
}
