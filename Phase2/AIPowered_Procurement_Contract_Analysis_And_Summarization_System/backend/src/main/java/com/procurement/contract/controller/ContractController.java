package com.procurement.contract.controller;

import com.procurement.contract.model.ContractRecord;
import com.procurement.contract.service.ContractService;
import com.procurement.contract.service.ContractStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173"})
@Validated
public class ContractController {

    private final ContractService contractService;
    private final ContractStorageService contractStorageService;

    public ContractController(
            ContractService contractService,
            ContractStorageService contractStorageService
    ) {
        this.contractService = contractService;
        this.contractStorageService = contractStorageService;
    }

    @GetMapping("/contracts")
    public List<ContractRecord> getContracts() {
        return contractStorageService.findAll();
    }

    @GetMapping("/contracts/{id}")
    public ResponseEntity<ContractRecord> getContract(@PathVariable Long id) {
        ContractRecord contract = contractService.findById(id);
        if (contract == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contract);
    }

    @PostMapping("/contracts/analyze")
    public ResponseEntity<ContractRecord> analyzeContract(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".pdf")
                && !filename.toLowerCase().endsWith(".docx"))) {
            return ResponseEntity.badRequest().build();
        }

        ContractRecord record = contractService.analyzeAndStore(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @DeleteMapping("/contracts/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) throws IOException {
        boolean removed = contractStorageService.delete(id);
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
