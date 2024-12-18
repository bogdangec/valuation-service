package co.quest.xms.valuation.infrastructure.rateLimiting;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.application.repository.UserRepository;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static co.quest.xms.valuation.util.TestConstants.*;
import static java.util.Optional.of;
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
        when(apiKeyRepository.findByKey(VALID_API_KEY.getKey())).thenReturn(of(VALID_API_KEY));
        when(userRepository.findById(VALID_API_KEY.getUserId())).thenReturn(of(User.builder().build()));

        assertTrue(rateLimiterService.isRequestAllowed(VALID_API_KEY));
        verify(apiKeyRepository, times(1)).save(VALID_API_KEY);
    }

    @Test
    void testIsRequestAllowed_ExceedsRateLimitPerMinute() {
        when(apiKeyRepository.findByKey(API_KEY_EXCEEDED_LIMIT_PER_MINUTE.getKey())).thenReturn(of(API_KEY_EXCEEDED_LIMIT_PER_MINUTE));
        when(userRepository.findById(API_KEY_EXCEEDED_LIMIT_PER_MINUTE.getUserId())).thenReturn(of(User.builder().build()));

        RuntimeException exception = assertThrows(RateLimitExceededException.class, () ->
                rateLimiterService.isRequestAllowed(API_KEY_EXCEEDED_LIMIT_PER_MINUTE)
        );
        assertEquals("Per-minute request limit exceeded for API key", exception.getMessage());
        verify(apiKeyRepository, never()).save(API_KEY_EXCEEDED_LIMIT_PER_MINUTE);
    }

    @Test
    void testIsRequestAllowed_ExceedsDailyLimit() {
        when(apiKeyRepository.findByKey(API_KEY_EXCEEDED_DAILY_LIMIT.getKey())).thenReturn(of(API_KEY_EXCEEDED_DAILY_LIMIT));
        when(userRepository.findById(API_KEY_EXCEEDED_DAILY_LIMIT.getUserId())).thenReturn(of(User.builder().build()));

        RuntimeException exception = assertThrows(RateLimitExceededException.class, () ->
                rateLimiterService.isRequestAllowed(API_KEY_EXCEEDED_DAILY_LIMIT)
        );
        assertEquals("Daily request limit exceeded for API key", exception.getMessage());
        verify(apiKeyRepository, never()).save(API_KEY_EXCEEDED_DAILY_LIMIT);
    }
}
