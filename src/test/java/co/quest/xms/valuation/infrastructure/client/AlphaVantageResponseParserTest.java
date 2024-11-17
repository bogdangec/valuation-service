package co.quest.xms.valuation.infrastructure.client;

import co.quest.xms.valuation.domain.model.StockPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static co.quest.xms.valuation.util.TestConstants.AAPL_SYMBOL;
import static co.quest.xms.valuation.util.TestUtils.loadJsonFromFile;
import static org.junit.jupiter.api.Assertions.*;

class AlphaVantageResponseParserTest {
    private AlphaVantageResponseParser parser;

    @BeforeEach
    void setUp() {
        parser = new AlphaVantageResponseParser(new ObjectMapper());
    }

    @Test
    void testParseStockPrices() throws IOException {
        // Given
        String mockResponse = loadJsonFromFile("aapl_time_series_daily.json");

        // When
        List<StockPrice> stockPrices = parser.parseStockPrices(mockResponse, AAPL_SYMBOL);

        // Then
        assertEquals(100, stockPrices.size());
        StockPrice firstPrice = stockPrices.getFirst();
        assertEquals(AAPL_SYMBOL, firstPrice.getSymbol());
        assertEquals(LocalDate.parse("2024-11-13").atStartOfDay(), firstPrice.getTimestamp());
        assertEquals(224.01, firstPrice.getOpen());
        assertEquals(226.65, firstPrice.getHigh());
        assertEquals(222.76, firstPrice.getLow());
        assertEquals(225.12, firstPrice.getClose());
        assertEquals(48566217, firstPrice.getVolume());
    }

    @Test
    void testParseRealtimeQuote() throws IOException {
        // Given
        String mockResponse = loadJsonFromFile("aapl_global_quote.json");

        // When
        StockPrice realtimeQuote = parser.parseRealtimeQuote(mockResponse, AAPL_SYMBOL);

        // Then
        assertEquals(AAPL_SYMBOL, realtimeQuote.getSymbol());
        assertEquals(LocalDate.now().atStartOfDay(), realtimeQuote.getTimestamp());
        assertEquals(224.01, realtimeQuote.getOpen());
        assertEquals(226.65, realtimeQuote.getHigh());
        assertEquals(222.76, realtimeQuote.getLow());
        assertEquals(225.12, realtimeQuote.getClose());
        assertEquals(48566217, realtimeQuote.getVolume());
    }

    @Test
    void testParseStockPricesWithInvalidData() {
        // Given
        String invalidJson = "{ \"invalid\": \"data\" }";  // Invalid JSON format

        // When
        List<StockPrice> stockPrices = parser.parseStockPrices(invalidJson, AAPL_SYMBOL);

        // Then
        assertTrue(stockPrices.isEmpty(), "Expected empty list on invalid JSON input");
    }

    @Test
    void testParseRealtimeQuoteWithInvalidData() {
        // Given
        String invalidJson = "{ \"invalid\": \"data\" }";  // Invalid JSON format

        // When
        StockPrice realtimeQuote = parser.parseRealtimeQuote(invalidJson, AAPL_SYMBOL);

        // Then
        assertNull(realtimeQuote.getSymbol());
        assertEquals(0.0, realtimeQuote.getOpen());
        assertEquals(0.0, realtimeQuote.getHigh());
        assertEquals(0.0, realtimeQuote.getLow());
        assertEquals(0.0, realtimeQuote.getClose());
        assertEquals(0L, realtimeQuote.getVolume());
    }
}
