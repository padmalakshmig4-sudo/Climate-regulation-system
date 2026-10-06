package com.aicity.climate.service;

import com.aicity.climate.model.ClimateLog;
import com.aicity.climate.repository.ClimateLogRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClimateLogService {

    private final ClimateLogRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public ClimateLogService(ClimateLogRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Saves a log entry and immediately pushes it to every browser tab
     * subscribed to /topic/logs, so the table updates live with no
     * refresh or re-poll needed.
     */
    public ClimateLog saveAndBroadcast(ClimateLog log) {
        ClimateLog saved = repository.save(log);
        messagingTemplate.convertAndSend("/topic/logs", saved);
        return saved;
    }
}
