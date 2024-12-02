package co.quest.xms.valuation.util;

import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.ACTIVE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constants {
    public static final String API_KEY_HEADER_NAME = "x-api-key";

    public static final ApiKey TEST_API_KEY = ApiKey.builder()
            .key("TEST_API_KEY")
            .expirationDate(LocalDateTime.now().plusDays(1))
            .status(ACTIVE)
            .rateLimitPerMinute(42)
            .build();
}
