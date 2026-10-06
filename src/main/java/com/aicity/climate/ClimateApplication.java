package com.aicity.climate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the AI City Climate Command backend.
 * Run this class directly from IntelliJ (right-click -> Run 'ClimateApplication'),
 * or from a terminal with: mvn spring-boot:run
 *
 * Once running, open http://localhost:8080 in your browser to see the dashboard.
 * The H2 database console is available at http://localhost:8080/h2-console
 */
@SpringBootApplication
@EnableScheduling
public class ClimateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClimateApplication.class, args);
    }
}
