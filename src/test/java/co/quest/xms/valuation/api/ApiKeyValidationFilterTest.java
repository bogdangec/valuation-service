package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.controller.StockPriceController;
import co.quest.xms.valuation.api.filter.ApiKeyValidationFilter;
import co.quest.xms.valuation.application.service.ApiKeyService;
import co.quest.xms.valuation.domain.exception.InvalidApiKeyException;
import co.quest.xms.valuation.domain.exception.RateLimitExceededException;
import co.quest.xms.valuation.domain.service.StockPriceService;
import co.quest.xms.valuation.infrastructure.rateLimiting.RateLimiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static co.quest.xms.valuation.util.Constants.API_KEY_HEADER_NAME;
import static co.quest.xms.valuation.util.TestConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ApiKeyValidationFilter.class, StockPriceController.class})
class ApiKeyValidationFilterTest {

    private static final String STOCK_PRICES_URL_FOR_AAPL = "/api/v1/stock-prices/AAPL";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockPriceService stockPriceService;

    @MockBean
    private RateLimiterService rateLimiterService;

    @MockBean
    private ApiKeyService apiKeyService;

    @BeforeEach
    void setUp() {
        when(apiKeyService.validateAndRetrieveApiKey(VALID_API_KEY_VALUE)).thenReturn(VALID_API_KEY);
        when(apiKeyService.validateAndRetrieveApiKey(EXPIRED_API_KEY_VALUE)).thenThrow(new InvalidApiKeyException("Expired API key"));
        when(apiKeyService.validateAndRetrieveApiKey(INACTIVE_API_KEY_VALUE)).thenThrow(new InvalidApiKeyException("Inactive API key"));

        when(apiKeyService.validateAndRetrieveApiKey(API_KEY_EXCEEDED_DAILY_LIMIT_VALUE)).thenReturn(API_KEY_EXCEEDED_DAILY_LIMIT);
        when(rateLimiterService.isRequestAllowed(API_KEY_EXCEEDED_DAILY_LIMIT)).thenThrow(new RateLimitExceededException("Daily request limit exceeded for API key"));

        when(apiKeyService.validateAndRetrieveApiKey(API_KEY_EXCEEDED_LIMIT_PER_MINUTE_VALUE)).thenReturn(API_KEY_EXCEEDED_LIMIT_PER_MINUTE);
        when(rateLimiterService.isRequestAllowed(API_KEY_EXCEEDED_LIMIT_PER_MINUTE)).thenThrow(new RateLimitExceededException("Per-minute request limit exceeded for API key"));
    }

    @Test
    void testValidApiKeyAllowsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, VALID_API_KEY_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testExpiredApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, EXPIRED_API_KEY_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInactiveApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, INACTIVE_API_KEY_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testApiKeyExceedsDailyLimitRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, API_KEY_EXCEEDED_DAILY_LIMIT_VALUE))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void testApiKeyExceedsLimitPerMinuteRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, API_KEY_EXCEEDED_LIMIT_PER_MINUTE_VALUE))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void testMissingApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL))
                .andExpect(status().isBadRequest());
    }
}
