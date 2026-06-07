package com.commodity.risk.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.commodity.risk.model.AnalysisRecord;
import com.commodity.risk.model.RiskAnalysisResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AnalysisStorageService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<AnalysisRecord> analyses = new ArrayList<AnalysisRecord>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Value("${storage.file.path}")
    private String storageFilePath;

    @Value("${history.max-items}")
    private int historyMaxItems;

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
        List<AnalysisRecord> stored = objectMapper.readValue(
                storageFile,
                new TypeReference<List<AnalysisRecord>>() {}
        );
        analyses.clear();
        analyses.addAll(stored);
        updateIdSequence();
    }

    private void updateIdSequence() {
        long maxId = analyses.stream()
                .mapToLong(record -> record.getId() == null ? 0L : record.getId())
                .max()
                .orElse(0L);
        idSequence.set(maxId + 1);
    }

    public synchronized AnalysisRecord save(String rawText, RiskAnalysisResult result) throws IOException {
        AnalysisRecord record = new AnalysisRecord(
                idSequence.getAndIncrement(),
                rawText,
                result.getCleanedText(),
                result.getEntities(),
                result.getRiskType(),
                result.getRiskScore(),
                result.getSeverity(),
                result.getAllScores(),
                result.getSentiment(),
                result.getSentimentScore(),
                result.getSummary(),
                Instant.now().toString()
        );

        analyses.add(0, record);
        trimHistory();
        persist();
        return record;
    }

    private void trimHistory() {
        while (analyses.size() > historyMaxItems) {
            analyses.remove(analyses.size() - 1);
        }
    }

    public synchronized List<AnalysisRecord> findAll() {
        List<AnalysisRecord> sorted = new ArrayList<AnalysisRecord>(analyses);
        sorted.sort(
                Comparator.comparing(
                        AnalysisRecord::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );
        return Collections.unmodifiableList(sorted);
    }

    public synchronized AnalysisRecord findById(Long id) {
        for (AnalysisRecord record : analyses) {
            if (record.getId().equals(id)) {
                return record;
            }
        }
        return null;
    }

    public synchronized boolean delete(Long id) throws IOException {
        boolean removed = analyses.removeIf(record -> record.getId().equals(id));
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
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, analyses);
    }
}
