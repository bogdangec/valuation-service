package co.quest.xms.valuation.api.dto;

import java.time.LocalDateTime;

public record ApiKeyDto(
        String key,
        int rateLimitPerMinute,
        int dailyLimit,
        LocalDateTime expirationDate,
        String status,
        String tier,
        int requestsMadeToday
) {
}
