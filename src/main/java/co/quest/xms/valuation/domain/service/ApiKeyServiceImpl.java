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
import static co.quest.xms.valuation.domain.model.ApiTier.FREE;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {
    private static final int INITIAL_RATE_LIMIT_PER_MINUTE = 10;
    private static final int INITIAL_RATE_LIMIT_PER_DAY = 100;
    private static final int INITIAL_ACTIVE_DAYS = 30 * 6;
    private static final int INITIAL_REQUESTS = 0;

    private final ApiKeyRepository apiKeyRepository;

    private static boolean isApiKeyInactiveOrExpired(ApiKey apiKey) {
        return INACTIVE.equals(apiKey.getStatus()) || apiKey.getExpirationDate().isBefore(now());
    }

    @Override
    public ApiKey generateApiKeyForUser(String userId) {
        return apiKeyRepository.save(buildApiKey(userId));
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

    @Override
    public ApiKey validateAndRetrieveApiKey(String apiKeyValue) {
        ApiKey apiKey = apiKeyRepository.findByKey(apiKeyValue)
                .orElseThrow(() -> new ApiKeyNotFoundException("API key not found: " + apiKeyValue));

        if (isApiKeyInactiveOrExpired(apiKey)) {
            throw new InvalidApiKeyException("API key is not active or expired: " + apiKey);
        }

        return apiKey;
    }

    private static ApiKey buildApiKey(String userId) {
        return ApiKey.builder()
                .key(randomUUID().toString())
                .rateLimitPerMinute(INITIAL_RATE_LIMIT_PER_MINUTE)
                .rateLimitPerDay(INITIAL_RATE_LIMIT_PER_DAY)
                .expirationDate(now().plusDays(INITIAL_ACTIVE_DAYS))
                .status(ACTIVE)
                .apiTier(FREE)
                .userId(userId)
                .requestsMadeToday(INITIAL_REQUESTS)
                .lastRequestDate(now().toLocalDate())
                .build();
    }
}
