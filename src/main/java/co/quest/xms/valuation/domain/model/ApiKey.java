package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "api_keys")
public class ApiKey {

    @Id
    private String id;
    @Field("api_key")
    private String key;
    @Field("rate_limit_per_minute")
    private int rateLimitPerMinute;
    @Field("rate_limit_per_day")
    private int rateLimitPerDay;
    @Field("expiration_date")
    private LocalDateTime expirationDate;
    @Field("status")
    private ApiKeyStatus status;
    @Field("api_tier")
    private ApiTier apiTier;
    @Field("user_id")
    private String userId;

    private int requestsMadeToday;  // Tracks requests made today
    private LocalDate lastRequestDate; // Tracks the last date when requests were made
}
