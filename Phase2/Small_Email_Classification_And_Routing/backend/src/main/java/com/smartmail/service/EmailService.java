package com.smartmail.service;

import com.smartmail.model.EmailMessage;
import com.smartmail.model.PredictionResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final MlServiceClient mlServiceClient;
    private final EmailStorageService emailStorageService;

    public EmailService(MlServiceClient mlServiceClient, EmailStorageService emailStorageService) {
        this.mlServiceClient = mlServiceClient;
        this.emailStorageService = emailStorageService;
    }

    public EmailMessage classifyAndStore(String emailText) throws IOException {
        PredictionResult prediction = mlServiceClient.predict(emailText);
        return emailStorageService.save(
                emailText,
                prediction.getCategory(),
                prediction.getConfidence(),
                "user"
        );
    }

    public List<EmailMessage> getAllEmails() {
        return emailStorageService.findAll();
    }

    public Map<String, List<EmailMessage>> getEmailsByCategory() {
        return emailStorageService.findGroupedByCategory();
    }

    public List<String> getCategories() {
        return mlServiceClient.getCategories();
    }

    public boolean deleteEmail(Long id) throws IOException {
        return emailStorageService.delete(id);
    }
}
