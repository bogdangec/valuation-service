package co.quest.xms.valuation.infrastructure.scheduler;

import co.quest.xms.valuation.infrastructure.rateLimiting.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimiterScheduler {

    private final RateLimiterService rateLimiterService;

    /**
     * Resets the request count every minute.
     */
    @Scheduled(fixedRate = 60000) // Every 60 seconds
    public void resetRateLimit() {
        rateLimiterService.resetRequestCounts();
    }
}
