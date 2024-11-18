package co.quest.xms.valuation.domain.service;

import co.quest.xms.valuation.application.client.AlphaVantageClient;
import co.quest.xms.valuation.application.publisher.StockPricePublisher;
import co.quest.xms.valuation.application.repository.StockPriceRepository;
import co.quest.xms.valuation.domain.model.StockPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static co.quest.xms.valuation.util.TestConstants.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockPriceServiceImplTest {
    @Mock
    private AlphaVantageClient alphaVantageClient;

    @Mock
    private StockPriceRepository stockPriceRepository;

    @Mock
    private StockPricePublisher stockPricePublisher;

    @InjectMocks
    private StockPriceServiceImpl stockPriceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHistoricalPrices() {
        // Given
        when(alphaVantageClient.fetchStockPrices(AAPL_SYMBOL)).thenReturn(MOCK_AAPL_PRICES);

        // When
        List<StockPrice> historicalPrices = stockPriceService.getHistoricalPrices(AAPL_SYMBOL);

        // Then
        assertEquals(MOCK_AAPL_PRICES.size(), historicalPrices.size());
        verify(stockPriceRepository, times(1)).saveAll(MOCK_AAPL_PRICES);
        verify(stockPricePublisher, times(MOCK_AAPL_PRICES.size())).publish(any(StockPrice.class));
    }

    @Test
    void testGetRealtimeQuote() {
        // Given
        when(alphaVantageClient.fetchRealtimeQuote(AAPL_SYMBOL)).thenReturn(MOCK_AAPL_PRICE);

        // When
        StockPrice realtimeQuote = stockPriceService.getRealtimeQuote(AAPL_SYMBOL);

        // Then
        assertEquals(MOCK_AAPL_PRICE, realtimeQuote);
        verify(stockPriceRepository, times(1)).save(MOCK_AAPL_PRICE);
        verify(stockPricePublisher, times(1)).publish(MOCK_AAPL_PRICE);
    }

    @Test
    void testGetHistoricalPricesWithInvalidSymbol() {
        // Given an invalid symbol that causes an API error
        String invalidSymbol = "INVALID";
        when(alphaVantageClient.fetchStockPrices(invalidSymbol)).thenThrow(new RuntimeException("API error"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> stockPriceService.getHistoricalPrices(invalidSymbol));

        // Then
        assertEquals("API error", exception.getMessage());
        verify(stockPriceRepository, never()).saveAll(any());
        verify(stockPricePublisher, never()).publish(any());
    }

    @Test
    void testGetHistoricalPricesWithEmptyResponse() {
        // Given a symbol that results in an empty response
        String symbol = "EMPTY";
        when(alphaVantageClient.fetchStockPrices(symbol)).thenReturn(emptyList());

        // When
        List<StockPrice> result = stockPriceService.getHistoricalPrices(symbol);

        // Then
        assertTrue(result.isEmpty());
        verify(stockPriceRepository).saveAll(emptyList());
        verify(stockPricePublisher, never()).publish(any());
    }

    @Test
    void testGetRealtimeQuoteWithInvalidSymbol() {
        // Given an invalid symbol that causes an API error
        String invalidSymbol = "INVALID";
        when(alphaVantageClient.fetchRealtimeQuote(invalidSymbol)).thenThrow(new RuntimeException("API error"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> stockPriceService.getRealtimeQuote(invalidSymbol));

        // Then
        assertEquals("API error", exception.getMessage());
        verify(stockPriceRepository, never()).save(any());
        verify(stockPricePublisher, never()).publish(any());
    }

    @Test
    void testGetRealtimeQuoteWithApiError() {
        // Given a symbol that causes an API error in the AlphaVantageClient
        String symbol = "ERROR";
        when(alphaVantageClient.fetchRealtimeQuote(symbol)).thenThrow(new RuntimeException("API error"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> stockPriceService.getRealtimeQuote(symbol));

        // Then
        assertEquals("API error", exception.getMessage());
        verify(stockPriceRepository, never()).save(any());
        verify(stockPricePublisher, never()).publish(any());
    }

    @Test
    void testDatabaseErrorDuringSave() {
        // Given a valid symbol but database throws an error
        String symbol = "AAPL";
        StockPrice stockPrice = StockPrice.builder().symbol(symbol).build();
        when(alphaVantageClient.fetchRealtimeQuote(symbol)).thenReturn(stockPrice);
        doThrow(new DataAccessException("Database error") {
        }).when(stockPriceRepository).save(stockPrice);

        // When
        Exception exception = assertThrows(DataAccessException.class, () -> stockPriceService.getRealtimeQuote(symbol));

        // Then
        assertEquals("Database error", exception.getMessage());
        verify(stockPricePublisher, never()).publish(any());
    }
}
