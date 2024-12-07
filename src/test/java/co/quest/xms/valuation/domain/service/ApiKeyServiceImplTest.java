package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.exception.ApiKeyNotFoundException;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static co.quest.xms.valuation.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(apiKeyRepository.findByKey(VALID_API_KEY_VALUE)).thenReturn(Optional.of(VALID_API_KEY));

        // Assert
        assertEquals(VALID_API_KEY, apiKeyServiceImpl.validateAndRetrieveApiKey(VALID_API_KEY_VALUE));
        verify(apiKeyRepository).findByKey(VALID_API_KEY_VALUE);
    }

    @Test
    void testValidateApiKey_InvalidKey_ThrowsException() {
        // Arrange
        String apiKey = "invalid-api-key";
        when(apiKeyRepository.findByKey(apiKey)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(ApiKeyNotFoundException.class, () ->
                apiKeyServiceImpl.validateAndRetrieveApiKey(apiKey)
        );
        assertEquals("API key not found: " + apiKey, exception.getMessage());
        verify(apiKeyRepository).findByKey(apiKey);
    }

    @Test
    void testValidateApiKey_InactiveKey_ThrowsException() {
        // Arrange
        when(apiKeyRepository.findByKey(INACTIVE_API_KEY_VALUE)).thenReturn(Optional.of(INACTIVE_API_KEY));

        // Act & Assert
        RuntimeException exception = assertThrows(InvalidApiKeyException.class, () ->
                apiKeyServiceImpl.validateAndRetrieveApiKey(INACTIVE_API_KEY_VALUE)
        );
        assertEquals("API key is not active or expired: " + INACTIVE_API_KEY, exception.getMessage());
        verify(apiKeyRepository).findByKey(INACTIVE_API_KEY_VALUE);
    }

    @Test
    void testValidateApiKey_ExpiredKey_ThrowsException() {
        // Arrange
        when(apiKeyRepository.findByKey(EXPIRED_API_KEY_VALUE)).thenReturn(Optional.of(EXPIRED_API_KEY));

        // Act & Assert
        RuntimeException exception = assertThrows(InvalidApiKeyException.class, () ->
                apiKeyServiceImpl.validateAndRetrieveApiKey(EXPIRED_API_KEY_VALUE)
        );
        assertEquals("API key is not active or expired: " + EXPIRED_API_KEY, exception.getMessage());
        verify(apiKeyRepository).findByKey(EXPIRED_API_KEY_VALUE);
    }
}
