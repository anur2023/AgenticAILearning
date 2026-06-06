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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EmailStorageService {

    private static final Map<String, Integer> PRIORITY_RANK = new LinkedHashMap<String, Integer>();

    static {
        PRIORITY_RANK.put("Critical", 4);
        PRIORITY_RANK.put("High", 3);
        PRIORITY_RANK.put("Medium", 2);
        PRIORITY_RANK.put("Low", 1);
    }

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

    public synchronized EmailMessage save(
            String emailText,
            String category,
            Double confidence,
            String priority,
            Double priorityConfidence,
            String source
    ) throws IOException {
        EmailMessage message = new EmailMessage(
                idSequence.getAndIncrement(),
                emailText,
                category,
                confidence,
                priority,
                priorityConfidence,
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
        Map<String, List<EmailMessage>> grouped = emails.stream()
                .collect(Collectors.groupingBy(
                        EmailMessage::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        grouped.replaceAll((category, messages) -> {
            List<EmailMessage> sorted = new ArrayList<EmailMessage>(messages);
            sorted.sort(
                    Comparator.comparingInt((EmailMessage email) -> priorityRank(email.getPriority())).reversed()
                            .thenComparing(
                                    EmailMessage::getCreatedAt,
                                    Comparator.nullsLast(Comparator.reverseOrder())
                            )
            );
            return sorted;
        });

        return grouped;
    }

    private int priorityRank(String priority) {
        if (priority == null) {
            return PRIORITY_RANK.getOrDefault("Medium", 2);
        }
        return PRIORITY_RANK.getOrDefault(priority, 0);
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
