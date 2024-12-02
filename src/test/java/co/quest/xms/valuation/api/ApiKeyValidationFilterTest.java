package co.quest.xms.valuation.api;

import co.quest.xms.valuation.api.filter.ApiKeyValidationFilter;
import co.quest.xms.valuation.application.repository.ApiKeyRepository;
import co.quest.xms.valuation.domain.service.StockPriceService;
import co.quest.xms.valuation.infrastructure.rateLimiting.RateLimiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static co.quest.xms.valuation.util.Constants.API_KEY_HEADER_NAME;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ApiKeyValidationFilter.class, StockPriceController.class})
class ApiKeyValidationFilterTest {

    private static final String VALID_API_KEY = "valid-api-key";
    private static final String EXPIRED_API_KEY = "expired-api-key";
    private static final String INACTIVE_API_KEY = "inactive-api-key";
    private static final String INVALID_API_KEY = "invalid-api-key";
    private static final String STOCK_PRICES_URL_FOR_AAPL = "/api/v1/stock-prices/AAPL";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockPriceService stockPriceService;

    @MockBean
    private RateLimiterService rateLimiterService;

    @MockBean
    private ApiKeyRepository apiKeyRepository;

    @BeforeEach
    void setUp() {
        when(rateLimiterService.isRequestAllowed(VALID_API_KEY)).thenReturn(true);
        when(rateLimiterService.isRequestAllowed(INVALID_API_KEY)).thenThrow(new IllegalArgumentException("Invalid API key"));
        when(rateLimiterService.isRequestAllowed(INACTIVE_API_KEY)).thenThrow(new IllegalStateException("API key is not active"));
//        when(apiKeyValidationService.validateApiKey(EXPIRED_API_KEY)).thenThrow(new RuntimeException("API key has expired"));

    }

    @Test
    void testValidApiKeyAllowsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, VALID_API_KEY))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, INVALID_API_KEY))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInactiveApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, INACTIVE_API_KEY))
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled("This test is ignored because it will be implemented for the ApiKeyValidationService")
    void testExpiredApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL)
                        .header(API_KEY_HEADER_NAME, EXPIRED_API_KEY))
                .andExpect(status().isForbidden());
    }

    @Test
    void testMissingApiKeyRejectsRequest() throws Exception {
        mockMvc.perform(get(STOCK_PRICES_URL_FOR_AAPL))
                .andExpect(status().isBadRequest());
    }
}
