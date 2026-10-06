package com.aicity.climate.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body accepted by POST /api/climate/log
 * Matches exactly what the dashboard's sendEvent() function sends:
 * { category, title, description, status }
 */
public class ClimateLogRequest {

    @NotBlank
    private String category;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String status;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
