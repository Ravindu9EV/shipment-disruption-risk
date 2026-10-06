package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.dto.ShipmentDTO;
import com.shipmentdisruptionrisk.sdrdemo.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shipments")
@RequiredArgsConstructor
public class ShipmentController {



    private final ShipmentService service;


    @PostMapping
    public void addShipment(ShipmentDTO data){
//        shipment.setId(data.getId());
//        shipment.setShipmentDate(data.getShipmentDate());
//        shipment.setOriginPort(data.getOriginPort());
//        shipment.setDistance(data.getDistance());
//        shipment.setTransportMode(data.getTransportMode());
//        shipment.setProductCategory(data.getProductCategory());
//        shipment.setDestination(data.getDestination());
//        shipment.setWeight(data.getWeight());
//        shipment.setFuelPrice(data.getFuelPrice());
//        shipment.setCarrierReliability(data.getCarrierReliability());
//        shipment.setLeadTime(data.getLeadTime());
//        shipment.setDisruptionOccurred(data.getDisruptionOccurred());
        service.add(data);
    }
}
