package com.smartmail.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmail.model.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EmailStorageService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<EmailMessage> emails = new ArrayList<EmailMessage>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Value("${storage.file.path}")
    private String storageFilePath;

    @PostConstruct
    public void init() throws IOException {
        File storageFile = new File(storageFilePath);
        if (storageFile.exists()) {
            loadFromFile(storageFile);
        } else {
            persist();
        }
    }

    private void loadFromFile(File storageFile) throws IOException {
        List<EmailMessage> stored = objectMapper.readValue(
                storageFile,
                new TypeReference<List<EmailMessage>>() {}
        );
        emails.clear();
        emails.addAll(
                stored.stream()
                        .filter(email -> !"seed".equalsIgnoreCase(email.getSource()))
                        .collect(Collectors.toList())
        );
        updateIdSequence();
        persist();
    }

    private void updateIdSequence() {
        long maxId = emails.stream()
                .mapToLong(email -> email.getId() == null ? 0L : email.getId())
                .max()
                .orElse(0L);
        idSequence.set(maxId + 1);
    }

    public synchronized EmailMessage save(String emailText, String category, Double confidence, String source) throws IOException {
        EmailMessage message = new EmailMessage(
                idSequence.getAndIncrement(),
                emailText,
                category,
                confidence,
                source,
                Instant.now().toString()
        );
        emails.add(message);
        persist();
        return message;
    }

    public synchronized List<EmailMessage> findAll() {
        return Collections.unmodifiableList(new ArrayList<EmailMessage>(emails));
    }

    public synchronized Map<String, List<EmailMessage>> findGroupedByCategory() {
        return emails.stream()
                .collect(Collectors.groupingBy(
                        EmailMessage::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public synchronized boolean delete(Long id) throws IOException {
        boolean removed = emails.removeIf(email -> email.getId().equals(id));
        if (removed) {
            persist();
        }
        return removed;
    }

    private void persist() throws IOException {
        File storageFile = new File(storageFilePath);
        File parent = storageFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, emails);
    }
}
