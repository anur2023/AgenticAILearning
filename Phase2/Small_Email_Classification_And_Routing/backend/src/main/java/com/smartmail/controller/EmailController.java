package com.smartmail.controller;

import com.smartmail.model.ClassifyRequest;
import com.smartmail.model.EmailMessage;
import com.smartmail.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
@Validated
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<String, String>();
        response.put("status", "ok");
        return response;
    }

    @GetMapping("/categories")
    public List<String> categories() {
        return emailService.getCategories();
    }

    @GetMapping("/emails")
    public List<EmailMessage> getEmails() {
        return emailService.getAllEmails();
    }

    @GetMapping("/emails/grouped")
    public Map<String, List<EmailMessage>> getGroupedEmails() {
        return emailService.getEmailsByCategory();
    }

    @PostMapping("/emails/classify")
    public ResponseEntity<EmailMessage> classifyEmail(@Valid @RequestBody ClassifyRequest request) throws IOException {
        EmailMessage saved = emailService.classifyAndStore(request.getEmailText().trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/emails/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) throws IOException {
        boolean deleted = emailService.deleteEmail(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
