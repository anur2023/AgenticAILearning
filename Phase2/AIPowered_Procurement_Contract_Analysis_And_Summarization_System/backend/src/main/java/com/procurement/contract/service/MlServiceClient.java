package com.procurement.contract.service;

import com.procurement.contract.model.AnalysisResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MlServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public MlServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AnalysisResult analyzeFile(MultipartFile file) throws IOException {
        String url = mlServiceUrl + "/analyze/file";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<MultiValueMap<String, Object>>(body, headers);

        return restTemplate.postForObject(url, request, AnalysisResult.class);
    }
}
