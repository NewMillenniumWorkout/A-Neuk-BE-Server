package com.example.aneukbeserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class AiClientService {
    private final RestTemplate restTemplate;
    private final int maxAttempts;
    private final long backoffMs;

    public AiClientService(
            RestTemplate restTemplate,
            @Value("${spring.ai.retry.max-attempts:2}") int maxAttempts,
            @Value("${spring.ai.retry.backoff-ms:200}") long backoffMs
    ) {
        this.restTemplate = restTemplate;
        this.maxAttempts = maxAttempts;
        this.backoffMs = backoffMs;
    }

    public <T, R> ResponseEntity<R> postWithRetry(String url, HttpEntity<T> entity, Class<R> responseType) {
        int attempts = Math.max(1, maxAttempts);
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                return restTemplate.postForEntity(url, entity, responseType);
            } catch (ResourceAccessException e) {
                if (attempt >= attempts) {
                    throw e;
                }
                sleepBackoff(attempt);
            }
        }
    }

    private void sleepBackoff(int attempt) {
        long sleepMs = backoffMs * attempt;
        if (sleepMs <= 0) {
            return;
        }
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
