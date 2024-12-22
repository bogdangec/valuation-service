package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.application.service.ApiKeyService;
import co.quest.xms.valuation.domain.exception.ApiKeyNotFoundException;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.INACTIVE;
import static co.quest.xms.valuation.domain.util.ApiKeyUtils.buildInitialApiKey;
import static co.quest.xms.valuation.domain.util.ApiKeyUtils.isApiKeyInactiveOrExpired;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKey generateApiKeyForUser(String userId) {
        return apiKeyRepository.save(buildInitialApiKey(userId));
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
}
