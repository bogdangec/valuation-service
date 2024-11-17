package co.quest.xms.valuation.infrastructure.client;

import co.quest.xms.valuation.domain.model.StockPrice;
import co.quest.xms.valuation.infrastructure.client.dto.GlobalQuoteData;
import co.quest.xms.valuation.infrastructure.client.dto.TimeSeriesData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Parser component for handling Alpha Vantage API responses.
 */
@Slf4j
@Component
public class AlphaVantageResponseParser {
    private final ObjectMapper objectMapper;

    public AlphaVantageResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Parses the Alpha Vantage response for historical stock data.
     */
    public List<StockPrice> parseStockPrices(String response, String symbol) {
        try {
            TimeSeriesData timeSeriesData = objectMapper.readValue(response, TimeSeriesData.class);
            return timeSeriesData.getTimeSeries().entrySet().stream()
                    .map(entry -> mapToStockPrice(entry.getKey(), entry.getValue(), symbol))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error parsing historical stock prices: {}", e.getMessage());
            return emptyList();
        }
    }

    /**
     * Parses the Alpha Vantage response for a single real-time stock quote.
     */
    public StockPrice parseRealtimeQuote(String response, String symbol) {
        try {
            GlobalQuoteData globalQuoteData = objectMapper.readValue(response, GlobalQuoteData.class);
            GlobalQuoteData.GlobalQuote quote = globalQuoteData.getGlobalQuote();

            return StockPrice.builder()
                    .symbol(symbol)
                    .timestamp(LocalDate.now().atStartOfDay())
                    .open(quote.getOpen())
                    .high(quote.getHigh())
                    .low(quote.getLow())
                    .close(quote.getPrice())
                    .volume(quote.getVolume())
                    .build();
        } catch (Exception e) {
            log.error("Error parsing real-time quote: {}", e.getMessage());
            return StockPrice.builder().build();
        }
    }

    // Helper method for mapping historical data entry to StockPrice
    private StockPrice mapToStockPrice(String date, TimeSeriesData.DailyData entry, String symbol) {
        return StockPrice.builder()
                .symbol(symbol)
                .timestamp(LocalDate.parse(date).atStartOfDay())
                .open(entry.getOpen())
                .high(entry.getHigh())
                .low(entry.getLow())
                .close(entry.getClose())
                .volume(entry.getVolume())
                .build();
    }
}
