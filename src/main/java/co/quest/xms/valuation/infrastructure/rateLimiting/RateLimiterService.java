package co.quest.xms.valuation.infrastructure.rateLimiting;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.model.ApiKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static co.quest.xms.valuation.domain.model.ApiTier.FREE;
import static java.time.Duration.between;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final ApiKeyRepository apiKeyRepository;
    private final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lastRequestTimestamps = new ConcurrentHashMap<>();

    @Value("${rate.limiter.window.seconds:60}")
    private int windowSeconds;

    public boolean isRequestAllowed(ApiKey apiKey) {
        if (FREE.equals(apiKey.getApiTier())) {
            checkDailyLimit(apiKey);
            checkPerMinuteLimit(apiKey.getKey(), apiKey.getRateLimitPerMinute());
        }
        recordRequest(apiKey.getKey(), apiKey);

        return true;
    }

    private void checkDailyLimit(ApiKey apiKey) {
        LocalDate today = LocalDate.now();

        if (!today.equals(apiKey.getLastRequestDate())) {
            apiKey.setRequestsMadeToday(0);
            apiKey.setLastRequestDate(today);
            apiKeyRepository.save(apiKey);
        }

        if (apiKey.getRequestsMadeToday() >= apiKey.getRateLimitPerDay()) {
            throw new RateLimitExceededException("Daily request limit exceeded for API key");
        }
    }

    private void checkPerMinuteLimit(String apiKeyValue, int rateLimitPerMinute) {
        int currentCount = requestCounts.getOrDefault(apiKeyValue, 0);
        if (currentCount >= rateLimitPerMinute) {
            throw new RateLimitExceededException("Per-minute request limit exceeded for API key");
        }
    }

    private void recordRequest(String apiKeyValue, ApiKey apiKey) {
        requestCounts.merge(apiKeyValue, 1, Integer::sum);
        lastRequestTimestamps.put(apiKeyValue, LocalDateTime.now());

        apiKey.setRequestsMadeToday(apiKey.getRequestsMadeToday() + 1);
        apiKeyRepository.save(apiKey);
    }

    @Scheduled(fixedRateString = "${rate.limiter.reset.interval.milliseconds:60000}")
    public void resetRequestCounts() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Reset counts at: {}", currentTime);
        requestCounts.forEach((apiKey, count) -> {
            LocalDateTime lastRequestTime = lastRequestTimestamps.get(apiKey);
            if (lastRequestTime == null || between(lastRequestTime, currentTime).getSeconds() >= windowSeconds) {
                requestCounts.remove(apiKey);
                lastRequestTimestamps.remove(apiKey);
                log.info("Removing API key: {}", apiKey);
            }
        });
    }
}
