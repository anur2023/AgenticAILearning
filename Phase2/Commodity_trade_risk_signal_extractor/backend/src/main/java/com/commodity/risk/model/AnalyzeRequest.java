package com.commodity.risk.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AnalyzeRequest {

    @NotBlank(message = "News text is required")
    @Size(min = 20, max = 5000, message = "Text must be between 20 and 5000 characters")
    private String text;

    public AnalyzeRequest() {
    }

    public AnalyzeRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
