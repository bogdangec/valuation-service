package co.quest.xms.valuation.infrastructure.client;

import co.quest.xms.valuation.domain.model.StockPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static co.quest.xms.valuation.util.TestConstants.*;
import static co.quest.xms.valuation.util.TestUtils.loadJsonFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AlphaVantageClientImplTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AlphaVantageResponseParser parser;

    @InjectMocks
    private AlphaVantageClientImpl alphaVantageClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alphaVantageClient = new AlphaVantageClientImpl(restTemplate, "test-api-key", parser);
    }

    @Test
    void testFetchStockPrices() throws IOException {
        // Given
        String mockResponse = loadJsonFromFile("aapl_time_series_daily.json");

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(parser.parseStockPrices(mockResponse, AAPL_SYMBOL)).thenReturn(MOCK_AAPL_PRICES);

        // When
        List<StockPrice> stockPrices = alphaVantageClient.fetchStockPrices(AAPL_SYMBOL);

        // Then
        assertEquals(MOCK_AAPL_PRICES, stockPrices);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(parser, times(1)).parseStockPrices(mockResponse, AAPL_SYMBOL);
    }

    @Test
    void testFetchRealtimeQuote() throws IOException {
        // Given
        String mockResponse = loadJsonFromFile("aapl_global_quote.json");

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(parser.parseRealtimeQuote(mockResponse, AAPL_SYMBOL)).thenReturn(MOCK_AAPL_PRICE);

        // When
        StockPrice realtimeQuote = alphaVantageClient.fetchRealtimeQuote(AAPL_SYMBOL);

        // Then
        assertEquals(MOCK_AAPL_PRICE, realtimeQuote);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(parser, times(1)).parseRealtimeQuote(mockResponse, AAPL_SYMBOL);
    }
}
