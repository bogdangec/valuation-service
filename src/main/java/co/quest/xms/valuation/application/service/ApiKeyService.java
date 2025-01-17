package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.exception.ApiKeyNotFoundException;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import co.quest.xms.valuation.domain.model.ApiKey;

import java.util.List;

/**
 * Port interface for managing API keys in the system.
 * This interface defines the contract for generating, retrieving, and deactivating API keys.
 * It should be implemented by service classes that encapsulate the business logic related to API key management.
 */
public interface ApiKeyService {

    /**
     * Generates a new API key for the given user.
     *
     * @param userId The ID of the user for whom the API key will be generated.
     * @return The generated {@link ApiKey} object.
     */
    ApiKey generateApiKeyForUser(String userId);

    /**
     * Retrieves all API keys associated with the given user.
     *
     * @param userId The ID of the user whose API keys are to be retrieved.
     * @return A list of {@link ApiKey} objects associated with the user.
     */
    List<ApiKey> getApiKeysForUser(String userId);

    /**
     * Deactivates a specific API key for the given user.
     * <p>
     * This operation marks the API key as inactive, preventing it from being used further.
     *
     * @param userId The ID of the user who owns the API key.
     * @param apiKey The API key to be deactivated.
     * @throws ApiKeyNotFoundException if the API key is not found or does not belong to the user.
     */
    void deactivateApiKeyForUser(String userId, String apiKey);

    /**
     * Validates the provided API key and retrieves the corresponding {@link ApiKey} entity.
     * <p>
     * If the API key is valid, it returns the associated {@link ApiKey} entity. If the API key is
     * invalid, an exception is thrown.
     *
     * @param apiKey the API key value to validate and retrieve.
     * @return the {@link ApiKey} entity associated with the provided key.
     * @throws ApiKeyNotFoundException if the API key does not exist.
     * @throws InvalidApiKeyException  if the API key exists but is not in an {@code ACTIVE} state or if the API key is expired.
     */
    ApiKey validateAndRetrieveApiKey(String apiKey);
}

