package co.quest.xms.valuation.infrastructure.rateLimiting;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.application.repository.UserRepository;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.model.ApiKey;
import co.quest.xms.valuation.domain.model.ApiKeyStatus;
import co.quest.xms.valuation.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static co.quest.xms.valuation.util.TestConstants.VALID_API_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimiterServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsRequestAllowed_WithinLimits() {
        when(apiKeyRepository.findByKey(VALID_API_KEY.getKey())).thenReturn(Optional.of(VALID_API_KEY));
        when(userRepository.findById(VALID_API_KEY.getUserId())).thenReturn(Optional.of(User.builder().build()));

        assertTrue(rateLimiterService.isRequestAllowed(VALID_API_KEY));
        verify(apiKeyRepository, times(1)).save(VALID_API_KEY);
    }

    @Test
    void testIsRequestAllowed_ExceedsRateLimitPerMinute() {
        ApiKey apiKey = ApiKey.builder()
                .id("1")
                .key("valid-api-key")
                .rateLimitPerMinute(0) // Rate limit set to 0 for testing
                .dailyLimit(100)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .status(ApiKeyStatus.ACTIVE)
                .userId("user123")
                .requestsMadeToday(10)
                .lastRequestDate(LocalDate.now())
                .build();

        when(apiKeyRepository.findByKey(apiKey.getKey())).thenReturn(Optional.of(apiKey));
        when(userRepository.findById(apiKey.getUserId())).thenReturn(Optional.of(User.builder().build()));

        RuntimeException exception = assertThrows(RateLimitExceededException.class, () ->
                rateLimiterService.isRequestAllowed(apiKey)
        );
        assertEquals("Per-minute request limit exceeded for API key", exception.getMessage());
        verify(apiKeyRepository, never()).save(apiKey);
    }

    @Test
    void testIsRequestAllowed_ExceedsDailyLimit() {
        ApiKey apiKey = ApiKey.builder()
                .id("1")
                .key("valid-api-key")
                .rateLimitPerMinute(5)
                .dailyLimit(10)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .status(ApiKeyStatus.ACTIVE)
                .userId("user123")
                .requestsMadeToday(11) // Already exceeded the daily limit
                .lastRequestDate(LocalDate.now())
                .build();

        when(apiKeyRepository.findByKey(apiKey.getKey())).thenReturn(Optional.of(apiKey));
        when(userRepository.findById(apiKey.getUserId())).thenReturn(Optional.of(User.builder().build()));

        RuntimeException exception = assertThrows(RateLimitExceededException.class, () ->
                rateLimiterService.isRequestAllowed(apiKey)
        );
        assertEquals("Daily request limit exceeded for API key", exception.getMessage());
        verify(apiKeyRepository, never()).save(apiKey);
    }
}
