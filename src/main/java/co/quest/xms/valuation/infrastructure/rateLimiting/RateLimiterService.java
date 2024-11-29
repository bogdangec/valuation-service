package co.quest.xms.valuation.infrastructure.rateLimiting;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    /**
     * Allows or denies a request based on the rate limit for an API key.
     *
     * @param apiKey    The API key for the request.
     * @param rateLimit The maximum allowed requests per time window.
     * @return True if the request is allowed, false otherwise.
     */
    public boolean allowRequest(String apiKey, int rateLimit) {
        requestCounts.putIfAbsent(apiKey, new AtomicInteger(0));
        int currentRequests = requestCounts.get(apiKey).incrementAndGet();

        return currentRequests <= rateLimit;
    }

    /**
     * Resets the request count for all API keys. To be scheduled periodically.
     */
    public void resetRequestCounts() {
        requestCounts.clear();
    }
}
