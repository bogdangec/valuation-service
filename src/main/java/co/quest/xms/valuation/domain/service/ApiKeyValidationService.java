package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.ACTIVE;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ApiKeyValidationService {

    private final ApiKeyRepository apiKeyRepository;

    private final RateLimiterService rateLimiterService;

    public boolean validateApiKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key).orElseThrow(() -> new RuntimeException("Invalid API key"));

        if (apiKey.getStatus() != ACTIVE) {
            throw new RuntimeException("API key is not active");
        }

        if (apiKey.getExpirationDate().isBefore(now())) {
            throw new RuntimeException("API key has expired");
        }

        // Check rate limit
        if (!rateLimiterService.allowRequest(key, apiKey.getRateLimit())) {
            throw new RuntimeException("Rate limit exceeded for API key");
        }

        return true;
    }
}
