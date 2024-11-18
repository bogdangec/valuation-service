package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "api_keys")
public class ApiKey {

    @Id
    @Field("_id")
    private String id;
    @Field("api_key")
    private String key;
    @Field("rate_limit")
    private int rateLimit;
    @Field("expiration_date")
    private LocalDateTime expirationDate;
    @Field("status")
    private ApiKeyStatus status;
    @Field("user_id")
    private String userId;
}
