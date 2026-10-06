package com.aicity.climate.service;

import com.aicity.climate.model.ClimateLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Every few seconds, generates one simulated event from a random subsystem
 * and pushes it live to the dashboard, so the table fills in on its own
 * (no button clicks required). This stands in for real sensor/AI feeds —
 * swap generateEvent() out for real integrations whenever you're ready.
 */
@Component
public class ClimateEventSimulator {

    private final ClimateLogService logService;
    private final Random random = new Random();

    private static final List<ClimateLog> TEMPLATES = List.of(
            new ClimateLog("TRANSPORT", "Transit Reroute", "Optimized Line 12 schedule", "SUCCESS"),
            new ClimateLog("TRANSPORT", "EV Fleet Sync", "Synced municipal EVs to smart grid", "SUCCESS"),
            new ClimateLog("TRANSPORT", "Congestion Alert", "Peak load detected on Line 4 corridor", "WARNING"),
            new ClimateLog("INFORMAL_SETTLEMENT", "Flood Alert", "High risk detected in Sector 4", "WARNING"),
            new ClimateLog("INFORMAL_SETTLEMENT", "Heat Index Warning", "Surface temp exceeded threshold in Sector 7", "WARNING"),
            new ClimateLog("INFORMAL_SETTLEMENT", "Relief Dispatch", "Cooling stations deployed to Sector 2", "RESOLVED"),
            new ClimateLog("MISINFORMATION", "Bot Mitigation", "Flagged automated disinfo cluster", "RESOLVED"),
            new ClimateLog("MISINFORMATION", "Viral Claim Detected", "Anti-sustainability post trending in Ward 9", "WARNING")
    );

    public ClimateEventSimulator(ClimateLogService logService) {
        this.logService = logService;
    }

    // Fires every 15 seconds. Adjust to taste.
    @Scheduled(fixedRate = 15000)
    public void generateEvent() {
        ClimateLog template = TEMPLATES.get(random.nextInt(TEMPLATES.size()));
        ClimateLog event = new ClimateLog(
                template.getCategory(),
                template.getTitle(),
                template.getDescription(),
                template.getStatus()
        );
        logService.saveAndBroadcast(event);
    }
}
