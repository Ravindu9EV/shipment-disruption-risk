package com.shipmentdisruptionrisk.sdrdemo.config;

import com.shipmentdisruptionrisk.sdrdemo.service.ShipmentIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataIngestionRunner implements CommandLineRunner {

    private final ShipmentIngestionService ingestionService;

    @Override
    public void run(String... args) throws Exception {
        // Trigger the ingestion when the app starts
        ingestionService.ingestShipments();
    }
}
