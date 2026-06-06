package com.procurement.contract.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procurement.contract.model.ContractRecord;
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
public class ContractStorageService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<ContractRecord> contracts = new ArrayList<ContractRecord>();
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
        List<ContractRecord> stored = objectMapper.readValue(
                storageFile,
                new TypeReference<List<ContractRecord>>() {}
        );
        contracts.clear();
        contracts.addAll(stored);
        updateIdSequence();
    }

    private void updateIdSequence() {
        long maxId = contracts.stream()
                .mapToLong(contract -> contract.getId() == null ? 0L : contract.getId())
                .max()
                .orElse(0L);
        idSequence.set(maxId + 1);
    }

    public synchronized ContractRecord save(
            String fileName,
            String supplier,
            String product,
            String quantity,
            String price,
            String deliveryDate,
            String paymentTerms,
            String summary
    ) throws IOException {
        ContractRecord record = new ContractRecord(
                idSequence.getAndIncrement(),
                fileName,
                supplier,
                product,
                quantity,
                price,
                deliveryDate,
                paymentTerms,
                summary,
                Instant.now().toString()
        );
        contracts.add(record);
        persist();
        return record;
    }

    public synchronized List<ContractRecord> findAll() {
        List<ContractRecord> sorted = new ArrayList<ContractRecord>(contracts);
        sorted.sort(
                Comparator.comparing(
                        ContractRecord::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );
        return Collections.unmodifiableList(sorted);
    }

    public synchronized ContractRecord findById(Long id) {
        for (ContractRecord contract : contracts) {
            if (contract.getId().equals(id)) {
                return contract;
            }
        }
        return null;
    }

    public synchronized boolean delete(Long id) throws IOException {
        boolean removed = contracts.removeIf(contract -> contract.getId().equals(id));
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
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, contracts);
    }
}
