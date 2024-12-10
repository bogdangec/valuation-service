package co.quest.xms.valuation.api.mapper;

import co.quest.xms.valuation.api.dto.ApiKeyDto;
import co.quest.xms.valuation.domain.model.ApiKey;

public final class ApiKeyMapper {

    private static final String EMPTY_KEY = "";

    public static ApiKeyDto toDto(ApiKey apiKey) {
        return new ApiKeyDto(apiKey.getKey(),
                apiKey.getRateLimitPerMinute(),
                apiKey.getDailyLimit(),
                apiKey.getExpirationDate(),
                apiKey.getStatus().toString(),
                apiKey.getRequestsMadeToday()
        );
    }

    public static ApiKeyDto toSafeDto(ApiKey apiKey) {
        return new ApiKeyDto(EMPTY_KEY,
                apiKey.getRateLimitPerMinute(),
                apiKey.getDailyLimit(),
                apiKey.getExpirationDate(),
                apiKey.getStatus().toString(),
                apiKey.getRequestsMadeToday()
        );
    }
}
