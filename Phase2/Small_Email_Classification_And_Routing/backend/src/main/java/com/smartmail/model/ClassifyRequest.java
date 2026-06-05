package com.smartmail.model;

import javax.validation.constraints.NotBlank;

public class ClassifyRequest {

    @NotBlank
    private String emailText;

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }
}
