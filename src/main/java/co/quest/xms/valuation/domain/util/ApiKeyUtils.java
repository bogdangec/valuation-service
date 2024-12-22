package co.quest.xms.valuation.domain.util;

import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.NoArgsConstructor;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.ACTIVE;
import static co.quest.xms.valuation.domain.model.ApiKeyStatus.INACTIVE;
import static co.quest.xms.valuation.domain.model.ApiTier.FREE;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ApiKeyUtils {
    private static final int INITIAL_RATE_LIMIT_PER_MINUTE = 10;
    private static final int INITIAL_RATE_LIMIT_PER_DAY = 100;
    private static final int INITIAL_ACTIVE_DAYS = 30 * 6;
    private static final int INITIAL_REQUESTS = 0;

    public static ApiKey buildInitialApiKey(String userId) {
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

    public static boolean isApiKeyInactiveOrExpired(ApiKey apiKey) {
        return INACTIVE.equals(apiKey.getStatus()) || apiKey.getExpirationDate().isBefore(now());
    }
}
