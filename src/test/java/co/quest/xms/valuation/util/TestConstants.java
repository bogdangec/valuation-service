package co.quest.xms.valuation.util;

import co.quest.xms.valuation.domain.model.StockPrice;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;

public final class TestConstants {

    public static final String AAPL_SYMBOL = "AAPL";

    public static final String GOOGL_SYMBOL = "GOOGL";

    public static final List<StockPrice> MOCK_AAPL_PRICES = asList(
            StockPrice.builder()
                    .symbol(AAPL_SYMBOL)
                    .timestamp(now())
                    .open(150.0)
                    .high(155.0)
                    .low(149.0)
                    .close(154.0)
                    .volume(10000L)
                    .build(),
            StockPrice.builder()
                    .symbol(AAPL_SYMBOL)
                    .timestamp(now().minusDays(1))
                    .open(148.0)
                    .high(152.0)
                    .low(147.0)
                    .close(150.0)
                    .volume(8000L)
                    .build()
    );
    public static final StockPrice MOCK_AAPL_PRICE = StockPrice.builder()
            .symbol(AAPL_SYMBOL)
            .timestamp(now())
            .open(155.0)
            .high(156.0)
            .low(153.0)
            .close(155.5)
            .volume(12000L)
            .build();

    public static final StockPrice MOCK_GOOGLE_PRICE = StockPrice.builder()
            .symbol(GOOGL_SYMBOL)
            .timestamp(LocalDateTime.now())
            .open(2750.0)
            .high(2775.0)
            .low(2725.0)
            .close(2760.0)
            .volume(15000L)
            .build();

    private TestConstants() {
    }

}
