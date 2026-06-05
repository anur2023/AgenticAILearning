package com.smartmail.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResult {

    private String category;
    private Double confidence;

    @JsonProperty("processed_text")
    private String processedText;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getProcessedText() {
        return processedText;
    }

    public void setProcessedText(String processedText) {
        this.processedText = processedText;
    }
}
