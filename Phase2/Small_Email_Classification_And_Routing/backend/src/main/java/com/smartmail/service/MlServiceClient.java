package com.smartmail.service;

import com.smartmail.model.PredictionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MlServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public MlServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PredictionResult predict(String emailText) {
        String url = mlServiceUrl + "/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<String, String>();
        body.put("email_text", emailText);

        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(body, headers);
        return restTemplate.postForObject(url, request, PredictionResult.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getCategories() {
        String url = mlServiceUrl + "/categories";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("categories")) {
            return Collections.emptyList();
        }
        return (List<String>) response.get("categories");
    }
}
