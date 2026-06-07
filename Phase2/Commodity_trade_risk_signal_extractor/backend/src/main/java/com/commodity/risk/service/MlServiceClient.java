package com.commodity.risk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.commodity.risk.model.AnalyzeRequest;
import com.commodity.risk.model.RiskAnalysisResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class MlServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public MlServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RiskAnalysisResult analyzeText(String text) {
        String url = mlServiceUrl + "/analyze";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AnalyzeRequest> request = new HttpEntity<AnalyzeRequest>(new AnalyzeRequest(text), headers);

        try {
            return restTemplate.postForObject(url, request, RiskAnalysisResult.class);
        } catch (HttpStatusCodeException ex) {
            RiskAnalysisResult errorResult = new RiskAnalysisResult();
            errorResult.setError(extractErrorMessage(ex.getResponseBodyAsString()));
            return errorResult;
        }
    }

    private String extractErrorMessage(String body) {
        if (body == null || body.isEmpty()) {
            return "ML service request failed";
        }
        try {
            JsonNode node = objectMapper.readTree(body);
            if (node.has("detail")) {
                return node.get("detail").asText();
            }
            if (node.has("error")) {
                return node.get("error").asText();
            }
        } catch (Exception ignored) {
            // fall through to raw body
        }
        return body;
    }
}
