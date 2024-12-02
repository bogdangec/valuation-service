package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.application.service.ApiKeyService;
import co.quest.xms.valuation.domain.exception.ApiKeyNotFoundException;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.ACTIVE;
import static co.quest.xms.valuation.domain.model.ApiKeyStatus.INACTIVE;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKey generateApiKeyForUser(String userId) {
        String key = randomUUID().toString();
        ApiKey apiKey = ApiKey.builder()
                .key(key)
                .rateLimitPerMinute(10)
                .dailyLimit(100)
                .expirationDate(now().plusDays(30))
                .status(ACTIVE)
                .userId(userId)
                .requestsMadeToday(0)
                .lastRequestDate(now().toLocalDate())
                .build();
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public List<ApiKey> getApiKeysForUser(String userId) {
        return apiKeyRepository.findByUserId(userId);
    }

    @Override
    public void deactivateApiKeyForUser(String userId, String apiKey) {
        ApiKey key = apiKeyRepository.findByKeyAndUserId(apiKey, userId)
                .orElseThrow(() -> new ApiKeyNotFoundException("API key not found or not associated with the user"));
        key.setStatus(INACTIVE);
        apiKeyRepository.save(key);
    }

    public void deactivateApiKey(String apiKey) {
        ApiKey key = apiKeyRepository.findByKey(apiKey).orElseThrow(() -> new ApiKeyNotFoundException("API key not found"));
        key.setStatus(INACTIVE);
        apiKeyRepository.save(key);
    }

    public boolean isApiKeyValid(String apiKey) {
        ApiKey key = apiKeyRepository.findByKey(apiKey).orElseThrow(() -> new InvalidApiKeyException("Invalid API key"));
        return key.getStatus() == ACTIVE && key.getExpirationDate().isAfter(now());
    }
}
