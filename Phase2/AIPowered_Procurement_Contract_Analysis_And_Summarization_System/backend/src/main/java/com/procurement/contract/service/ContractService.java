package com.procurement.contract.service;

import com.procurement.contract.model.AnalysisResult;
import com.procurement.contract.model.ContractRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ContractService {

    private final MlServiceClient mlServiceClient;
    private final ContractStorageService contractStorageService;

    public ContractService(
            MlServiceClient mlServiceClient,
            ContractStorageService contractStorageService
    ) {
        this.mlServiceClient = mlServiceClient;
        this.contractStorageService = contractStorageService;
    }

    public ContractRecord analyzeAndStore(MultipartFile file) throws IOException {
        AnalysisResult analysis = mlServiceClient.analyzeFile(file);
        return contractStorageService.save(
                file.getOriginalFilename(),
                analysis.getSupplier(),
                analysis.getProduct(),
                analysis.getQuantity(),
                analysis.getPrice(),
                analysis.getDeliveryDate(),
                analysis.getPaymentTerms(),
                analysis.getSummary()
        );
    }

    public ContractRecord findById(Long id) {
        return contractStorageService.findById(id);
    }
}
