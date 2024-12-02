package co.quest.xms.valuation.infrastructure.rateLimiting;

import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.application.repository.UserRepository;
import co.quest.xms.valuation.domain.exception.ApiKeyNotFoundException;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.exception.UserNotFoundException;
import co.quest.xms.valuation.domain.model.ApiKey;
import co.quest.xms.valuation.domain.model.ApiKeyStatus;
import co.quest.xms.valuation.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final ApiKeyRepository apiKeyRepository;

    private final UserRepository userRepository;

    private final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lastRequestTimestamps = new ConcurrentHashMap<>();

    @Value("${rate.limiter.window.seconds:60}")
    private int windowSeconds;

    public boolean isRequestAllowed(String apiKeyValue) {
        ApiKey apiKey = apiKeyRepository.findByKey(apiKeyValue)
                .orElseThrow(() -> new ApiKeyNotFoundException("API Key not found"));

        User user = userRepository.findById(apiKey.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (apiKey.getStatus() != ApiKeyStatus.ACTIVE) {
            throw new IllegalStateException("API key is not active");
        }

        LocalDate today = LocalDate.now();

        // Reset daily limits if it's a new day
        if (!today.equals(apiKey.getLastRequestDate())) {
            apiKey.setRequestsMadeToday(0);
            apiKey.setLastRequestDate(today);
            apiKeyRepository.save(apiKey);
        }

        // Check daily limit
        if (apiKey.getRequestsMadeToday() >= apiKey.getDailyLimit()) {
            throw new RateLimitExceededException("Daily request limit exceeded for API key");
        }

        // Check per-minute limit
        int currentCount = requestCounts.getOrDefault(apiKeyValue, 0);
        if (currentCount >= apiKey.getRateLimitPerMinute()) {
            throw new RateLimitExceededException("Per-minute request limit exceeded for API key");
        }

        // Update request counts and timestamps
        requestCounts.put(apiKeyValue, currentCount + 1);
        lastRequestTimestamps.put(apiKeyValue, LocalDateTime.now());

        // Update daily request count
        apiKey.setRequestsMadeToday(apiKey.getRequestsMadeToday() + 1);
        apiKeyRepository.save(apiKey);

        return true;
    }

    @Scheduled(fixedRateString = "${rate.limiter.reset.interval.milliseconds:60000}")
    public void resetRequestCounts() {
        LocalDateTime currentTime = LocalDateTime.now();
        requestCounts.forEach((apiKey, count) -> {
            LocalDateTime lastRequestTime = lastRequestTimestamps.get(apiKey);
            if (lastRequestTime == null || Duration.between(lastRequestTime, currentTime).getSeconds() >= windowSeconds) {
                requestCounts.remove(apiKey);
                lastRequestTimestamps.remove(apiKey);
            }
        });
    }
}

