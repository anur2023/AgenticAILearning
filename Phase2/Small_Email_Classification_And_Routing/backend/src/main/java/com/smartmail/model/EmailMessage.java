package com.smartmail.model;

public class EmailMessage {

    private Long id;
    private String emailText;
    private String category;
    private Double confidence;
    private String source;
    private String createdAt;

    public EmailMessage() {
    }

    public EmailMessage(Long id, String emailText, String category, Double confidence, String source, String createdAt) {
        this.id = id;
        this.emailText = emailText;
        this.category = category;
        this.confidence = confidence;
        this.source = source;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
