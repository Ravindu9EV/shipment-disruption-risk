package com.shipmentdisruptionrisk.sdrdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class SdrdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdrdemoApplication.class, args);
	}

}
