package com.commodity.risk.service;

import com.commodity.risk.model.AnalysisRecord;
import com.commodity.risk.model.RiskAnalysisResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnalysisService {

    private final MlServiceClient mlServiceClient;
    private final AnalysisStorageService analysisStorageService;

    public AnalysisService(
            MlServiceClient mlServiceClient,
            AnalysisStorageService analysisStorageService
    ) {
        this.mlServiceClient = mlServiceClient;
        this.analysisStorageService = analysisStorageService;
    }

    public AnalysisRecord analyzeAndStore(String rawText) throws IOException {
        RiskAnalysisResult result = mlServiceClient.analyzeText(rawText);
        if (result == null) {
            throw new IllegalStateException("ML service returned no result");
        }
        if (result.getError() != null && !result.getError().isEmpty()) {
            throw new IllegalArgumentException(result.getError());
        }
        return analysisStorageService.save(rawText, result);
    }

    public AnalysisRecord findById(Long id) {
        return analysisStorageService.findById(id);
    }
}
