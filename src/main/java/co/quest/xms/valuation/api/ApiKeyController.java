package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.dto.ApiKeyDto;
import co.quest.xms.valuation.api.mapper.ApiKeyMapper;
import co.quest.xms.valuation.application.service.ApiKeyService;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.quest.xms.valuation.api.mapper.ApiKeyMapper.toDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/key")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * Generates a new API key for the authenticated user.
     *
     * @param userId The ID of the user (should come from authentication context).
     * @return The generated API key.
     */
    @PostMapping
    public ResponseEntity<ApiKeyDto> generateApiKey(@RequestHeader("x-user-id") String userId) {
        ApiKey apiKey = apiKeyService.generateApiKeyForUser(userId);
        return ResponseEntity.ok(toDto(apiKey));
    }

    /**
     * Retrieves all API keys associated with the authenticated user.
     *
     * @param userId The ID of the user (should come from authentication context).
     * @return A list of API keys.
     */
    @GetMapping
    public ResponseEntity<List<ApiKeyDto>> getApiKeys(@RequestHeader("x-user-id") String userId) {
        List<ApiKey> apiKeys = apiKeyService.getApiKeysForUser(userId);
        return ResponseEntity.ok(apiKeys.stream().map(ApiKeyMapper::toDto).toList());
    }

    /**
     * Deactivates an API key for the authenticated user.
     *
     * @param userId The ID of the user (should come from authentication context).
     * @param apiKey The API key to deactivate.
     * @return A success response.
     */
    @DeleteMapping("/{apiKey}")
    public ResponseEntity<Void> deactivateApiKey(
            @RequestHeader("x-user-id") String userId,
            @PathVariable String apiKey
    ) {
        apiKeyService.deactivateApiKeyForUser(userId, apiKey);
        return ResponseEntity.noContent().build();
    }
}
