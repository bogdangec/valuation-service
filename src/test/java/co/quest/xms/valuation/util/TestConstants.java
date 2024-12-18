package co.quest.xms.valuation.util;

import co.quest.xms.valuation.domain.model.ApiKey;
import co.quest.xms.valuation.domain.model.ApiKeyStatus;
import co.quest.xms.valuation.domain.model.StockPrice;

import java.time.LocalDate;
import java.util.List;

import static co.quest.xms.valuation.domain.model.ApiKeyStatus.ACTIVE;
import static co.quest.xms.valuation.domain.model.ApiKeyStatus.INACTIVE;
import static co.quest.xms.valuation.domain.model.ApiTier.FREE;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;

public final class TestConstants {

    public static final String AAPL_SYMBOL = "AAPL";

    public static final String GOOGL_SYMBOL = "GOOGL";

    public static final List<StockPrice> MOCK_AAPL_PRICES = asList(
            StockPrice.builder()
                    .symbol(AAPL_SYMBOL)
                    .timestamp(now())
                    .open(150.0)
                    .high(155.0)
                    .low(149.0)
                    .close(154.0)
                    .volume(10000L)
                    .build(),
            StockPrice.builder()
                    .symbol(AAPL_SYMBOL)
                    .timestamp(now().minusDays(1))
                    .open(148.0)
                    .high(152.0)
                    .low(147.0)
                    .close(150.0)
                    .volume(8000L)
                    .build()
    );
    public static final StockPrice MOCK_AAPL_PRICE = StockPrice.builder()
            .symbol(AAPL_SYMBOL)
            .timestamp(now())
            .open(155.0)
            .high(156.0)
            .low(153.0)
            .close(155.5)
            .volume(12000L)
            .build();

    public static final StockPrice MOCK_GOOGLE_PRICE = StockPrice.builder()
            .symbol(GOOGL_SYMBOL)
            .timestamp(now())
            .open(2750.0)
            .high(2775.0)
            .low(2725.0)
            .close(2760.0)
            .volume(15000L)
            .build();

    public static final ApiKey VALID_API_KEY = ApiKey.builder()
            .id("1")
            .key("valid-api-key")
            .rateLimitPerMinute(5)
            .rateLimitPerDay(100)
            .expirationDate(now().plusDays(1))
            .status(ApiKeyStatus.ACTIVE)
            .userId("user123")
            .requestsMadeToday(10)
            .lastRequestDate(LocalDate.now())
            .build();

    public static final ApiKey EXPIRED_API_KEY = ApiKey.builder()
            .id("2")
            .key("expired-api-key")
            .rateLimitPerMinute(5)
            .rateLimitPerDay(100)
            .expirationDate(now().minusDays(1))
            .status(ACTIVE)
            .userId("user123")
            .requestsMadeToday(10)
            .lastRequestDate(LocalDate.now())
            .build();


    public static final ApiKey INACTIVE_API_KEY = ApiKey.builder()
            .id("3")
            .key("inactive-api-key")
            .rateLimitPerMinute(5)
            .rateLimitPerDay(100)
            .expirationDate(now().plusDays(1))
            .status(INACTIVE)
            .userId("user123")
            .requestsMadeToday(10)
            .lastRequestDate(LocalDate.now())
            .build();

    public static final ApiKey API_KEY_EXCEEDED_LIMIT_PER_MINUTE = ApiKey.builder()
            .id("4")
            .key("active-api-key")
            .rateLimitPerMinute(0) // Already exceeded the limit per minute
            .rateLimitPerDay(100)
            .expirationDate(now().plusDays(1))
            .status(ACTIVE)
            .apiTier(FREE)
            .userId("user123")
            .requestsMadeToday(10)
            .lastRequestDate(LocalDate.now())
            .build();

    public static final ApiKey API_KEY_EXCEEDED_DAILY_LIMIT = ApiKey.builder()
            .id("5")
            .key("active-api-key")
            .rateLimitPerMinute(10)
            .rateLimitPerDay(100)
            .expirationDate(now().plusDays(1))
            .status(ACTIVE)
            .apiTier(FREE)
            .userId("user123")
            .requestsMadeToday(111) // Already exceeded the daily limit
            .lastRequestDate(LocalDate.now())
            .build();

    public static final String VALID_API_KEY_VALUE = VALID_API_KEY.getKey();

    public static final String EXPIRED_API_KEY_VALUE = EXPIRED_API_KEY.getKey();

    public static final String INACTIVE_API_KEY_VALUE = INACTIVE_API_KEY.getKey();

    public static final String API_KEY_EXCEEDED_DAILY_LIMIT_VALUE = API_KEY_EXCEEDED_DAILY_LIMIT.getKey();

    public static final String API_KEY_EXCEEDED_LIMIT_PER_MINUTE_VALUE = API_KEY_EXCEEDED_LIMIT_PER_MINUTE.getKey();


    private TestConstants() {
    }

}
