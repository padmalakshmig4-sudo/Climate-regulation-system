package com.aicity.climate.controller;

import com.aicity.climate.model.ClimateLog;
import com.aicity.climate.model.ClimateLogRequest;
import com.aicity.climate.repository.ClimateLogRepository;
import com.aicity.climate.service.ClimateLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API backing the AI City Climate Command dashboard.
 * Endpoints match exactly what index.html already calls:
 *   GET  /api/climate/logs       -> list all logs, newest first
 *   POST /api/climate/log        -> create a new log entry
 *   GET  /api/climate/init-demo  -> seed some demo data (idempotent-ish)
 */
@RestController
@RequestMapping("/api/climate")
public class ClimateController {

    private final ClimateLogRepository repository;
    private final ClimateLogService logService;

    public ClimateController(ClimateLogRepository repository, ClimateLogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    @GetMapping("/logs")
    public List<ClimateLog> getLogs() {
        return repository.findAllByOrderByIdDesc();
    }

    @PostMapping("/log")
    public ResponseEntity<ClimateLog> createLog(@Valid @RequestBody ClimateLogRequest request) {
        ClimateLog log = new ClimateLog(
                request.getCategory(),
                request.getTitle(),
                request.getDescription(),
                request.getStatus()
        );
        ClimateLog saved = logService.saveAndBroadcast(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/init-demo")
    public ResponseEntity<Map<String, Object>> initDemo() {
        // Only seed if the table is empty, so refreshing the page doesn't duplicate rows.
        if (repository.count() == 0) {
            logService.saveAndBroadcast(new ClimateLog(
                    "TRANSPORT", "Transit Reroute", "Optimized Line 12 schedule", "SUCCESS"));
            logService.saveAndBroadcast(new ClimateLog(
                    "INFORMAL_SETTLEMENT", "Flood Alert", "High risk detected in Sector 4", "WARNING"));
            logService.saveAndBroadcast(new ClimateLog(
                    "MISINFORMATION", "Bot Mitigation", "Flagged automated disinfo cluster", "RESOLVED"));
            logService.saveAndBroadcast(new ClimateLog(
                    "TRANSPORT", "EV Fleet Sync", "Synced 42 municipal EVs to smart grid", "SUCCESS"));
            logService.saveAndBroadcast(new ClimateLog(
                    "INFORMAL_SETTLEMENT", "Heat Index Warning", "Sector 7 surface temp exceeded threshold", "WARNING"));
        }
        return ResponseEntity.ok(Map.of(
                "seeded", true,
                "totalLogs", repository.count()
        ));
    }

    @DeleteMapping("/logs")
    public ResponseEntity<Void> clearLogs() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
