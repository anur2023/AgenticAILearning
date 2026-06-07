package com.commodity.risk.model;

import java.util.Map;

public class AnalysisRecord {

    private Long id;
    private String rawText;
    private String cleanedText;
    private Entities entities;
    private String riskType;
    private Double riskScore;
    private String severity;
    private Map<String, Double> allScores;
    private String sentiment;
    private Double sentimentScore;
    private String summary;
    private String createdAt;

    public AnalysisRecord() {
    }

    public AnalysisRecord(
            Long id,
            String rawText,
            String cleanedText,
            Entities entities,
            String riskType,
            Double riskScore,
            String severity,
            Map<String, Double> allScores,
            String sentiment,
            Double sentimentScore,
            String summary,
            String createdAt
    ) {
        this.id = id;
        this.rawText = rawText;
        this.cleanedText = cleanedText;
        this.entities = entities;
        this.riskType = riskType;
        this.riskScore = riskScore;
        this.severity = severity;
        this.allScores = allScores;
        this.sentiment = sentiment;
        this.sentimentScore = sentimentScore;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getCleanedText() {
        return cleanedText;
    }

    public void setCleanedText(String cleanedText) {
        this.cleanedText = cleanedText;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public Double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Map<String, Double> getAllScores() {
        return allScores;
    }

    public void setAllScores(Map<String, Double> allScores) {
        this.allScores = allScores;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(Double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
