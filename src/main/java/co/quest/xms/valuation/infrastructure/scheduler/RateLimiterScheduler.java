package co.quest.xms.valuation.infrastructure.scheduler;

import co.quest.xms.valuation.domain.service.RateLimiterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterScheduler {

    private final RateLimiterService rateLimiterService;

    public RateLimiterScheduler(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    /**
     * Resets the request count every minute.
     */
    @Scheduled(fixedRate = 60000) // Every 60 seconds
    public void resetRateLimit() {
        rateLimiterService.resetRequestCounts();
    }
}
