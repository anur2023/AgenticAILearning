package com.commodity.risk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class RiskAnalysisResult {

    @JsonProperty("cleaned_text")
    private String cleanedText;
    private Entities entities;
    @JsonProperty("risk_type")
    private String riskType;

    @JsonProperty("risk_score")
    private Double riskScore;

    private String severity;

    @JsonProperty("all_scores")
    private Map<String, Double> allScores;

    private String sentiment;

    @JsonProperty("sentiment_score")
    private Double sentimentScore;
    private String summary;
    private String error;

    public RiskAnalysisResult() {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
