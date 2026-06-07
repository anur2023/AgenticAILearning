package com.commodity.risk.controller;

import com.commodity.risk.model.AnalysisRecord;
import com.commodity.risk.model.AnalyzeRequest;
import com.commodity.risk.service.AnalysisService;
import com.commodity.risk.service.AnalysisStorageService;
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
public class AnalysisController {

    private final AnalysisService analysisService;
    private final AnalysisStorageService analysisStorageService;

    public AnalysisController(
            AnalysisService analysisService,
            AnalysisStorageService analysisStorageService
    ) {
        this.analysisService = analysisService;
        this.analysisStorageService = analysisStorageService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<String, String>();
        response.put("status", "ok");
        return response;
    }

    @GetMapping("/analyses")
    public List<AnalysisRecord> getAnalyses() {
        return analysisStorageService.findAll();
    }

    @GetMapping("/analyses/{id}")
    public ResponseEntity<AnalysisRecord> getAnalysis(@PathVariable Long id) {
        AnalysisRecord record = analysisService.findById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeNews(@Valid @RequestBody AnalyzeRequest request) {
        try {
            AnalysisRecord record = analysisService.analyzeAndStore(request.getText());
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException | IllegalStateException ex) {
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "Analysis failed. Ensure the ML service is running on port 8000.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    @DeleteMapping("/analyses/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) throws IOException {
        boolean removed = analysisStorageService.delete(id);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
