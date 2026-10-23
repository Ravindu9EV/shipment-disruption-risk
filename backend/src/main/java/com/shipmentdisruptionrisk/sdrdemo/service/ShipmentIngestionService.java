package com.shipmentdisruptionrisk.sdrdemo.service;

import com.opencsv.CSVReaderHeaderAware;
import com.shipmentdisruptionrisk.sdrdemo.entity.Shipment;
import com.shipmentdisruptionrisk.sdrdemo.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentIngestionService {

    private final ShipmentRepository shipmentRepository;

    public void ingestShipments() {
        log.info("Starting shipment ingestion...");

        // 1. Create a Set of existing shipment IDs to prevent duplicates
        Set<String> existingIds = new HashSet<>();
        shipmentRepository.findAll().forEach(s -> existingIds.add(s.getShipmentId()));

        // 2. Set up the date formatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int successCount = 0;
        int skipCount = 0;

        // 3. Open the file
        try (Reader reader = new InputStreamReader(new ClassPathResource("data/global_supply_chain_risk_2026.csv").getInputStream());
             CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader)) {

            Map<String, String> row;

            // 4. Read line by line
            while ((row = csvReader.readMap()) != null) {
                String shipmentId = row.get("Shipment_ID");
                log.info("CSV Headers: " + row.keySet());
                // Check for duplicates
                if (shipmentId == null || existingIds.contains(shipmentId)) {
                    skipCount++;
                    continue;
                }

                // 5. Try-Catch for malformed rows
                try {
                    Shipment shipment = Shipment.builder()
                        .shipmentId(row.get("Shipment_ID").trim())
                        .shipmentDate(LocalDate.parse(row.get("Date").trim(), dateFormatter))
                        .originPort(row.get("Origin_Port").trim())
                        .destination(row.get("Destination_Port").trim()) // Changed
                        .transportMode(row.get("Transport_Mode").trim())
                        .productCategory(row.get("Product_Category").trim())
                        .distance(Double.parseDouble(row.get("Distance_km").trim())) // Changed
                        .weight(Double.parseDouble(row.get("Weight_MT").trim())) // Changed
                        .fuelPrice(Double.parseDouble(row.get("Fuel_Price_Index").trim())) // Changed
                        .carrierReliability(Double.parseDouble(row.get("Carrier_Reliability_Score").trim())) // Changed
                        .leadTime(Double.parseDouble(row.get("Lead_Time_Days").trim())) // Changed
                        .disruptionOccurred(false)
                        .build();

                    shipmentRepository.save(shipment);
                    existingIds.add(shipmentId);
                    successCount++;

                } catch (Exception e) {
                    log.warn("Skipping malformed row for Shipment_ID: {}. Error: {}", shipmentId, e.getMessage());
                    skipCount++;
                }
            }

        } catch (Exception e) {
            log.error("Failed to read the CSV file", e);
        }

        log.info("Ingestion complete. Saved: {}, Skipped: {}", successCount, skipCount);
    }
}
