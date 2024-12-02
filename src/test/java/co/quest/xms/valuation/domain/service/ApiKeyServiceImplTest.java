package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.model.ApiKey;
import co.quest.xms.valuation.domain.model.ApiKeyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApiKeyServiceImplTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyServiceImpl apiKeyServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateApiKey_ValidKey_AllowsRequest() {
        // Arrange
        String apiKey = "valid-api-key";
        ApiKey mockApiKey = ApiKey.builder()
                .key(apiKey)
                .status(ApiKeyStatus.ACTIVE)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .build();

        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.of(mockApiKey));

        // Act
        boolean result = apiKeyServiceImpl.isApiKeyValid(apiKey);

        // Assert
        assertTrue(result);
        verify(apiKeyRepository).findByKey(apiKey);
    }

    @Test
    void testValidateApiKey_InvalidKey_ThrowsException() {
        // Arrange
        String apiKey = "invalid-api-key";

        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                apiKeyServiceImpl.isApiKeyValid(apiKey)
        );
        assertEquals("Invalid API key", exception.getMessage());
        verify(apiKeyRepository).findByKey(apiKey);
    }

    @Test
    void testValidateApiKey_InactiveKey_ThrowsException() {
        // Arrange
        String apiKey = "inactive-api-key";
        ApiKey mockApiKey = ApiKey.builder()
                .key(apiKey)
                .status(ApiKeyStatus.INACTIVE)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .build();

        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.of(mockApiKey));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                apiKeyServiceImpl.isApiKeyValid(apiKey)
        );
        assertEquals("API key is not active", exception.getMessage());
        verify(apiKeyRepository).findByKey(apiKey);
    }

    @Test
    void testValidateApiKey_ExpiredKey_ThrowsException() {
        // Arrange
        String apiKey = "expired-api-key";
        ApiKey mockApiKey = ApiKey.builder()
                .key(apiKey)
                .status(ApiKeyStatus.ACTIVE)
                .expirationDate(LocalDateTime.now().minusDays(1))
                .build();

        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.of(mockApiKey));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                apiKeyServiceImpl.isApiKeyValid(apiKey)
        );
        assertEquals("API key has expired", exception.getMessage());
        verify(apiKeyRepository).findByKey(apiKey);
    }

    @Test
    @Disabled("This test is ignored because it will be implemented for the RateLimiterService")
    void testValidateApiKey_RateLimitExceeded_ThrowsException() {
        // Arrange
        String apiKey = "rate-limit-exceeded-key";
        ApiKey mockApiKey = ApiKey.builder()
                .key(apiKey)
                .status(ApiKeyStatus.ACTIVE)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .build();

        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.of(mockApiKey));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                apiKeyServiceImpl.isApiKeyValid(apiKey)
        );
        assertEquals("Rate limit exceeded for API key", exception.getMessage());
        verify(apiKeyRepository).findByKey(apiKey);
    }
}
