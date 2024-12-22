package co.quest.xms.valuation.api.controller;

import co.quest.xms.valuation.domain.model.StockPrice;
import co.quest.xms.valuation.domain.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/stock-prices")
@RequiredArgsConstructor
public class StockPriceController {

    private final StockPriceService stockPriceService;

    /**
     * Endpoint to fetch historical stock prices.
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<List<StockPrice>> getHistoricalPrices(@PathVariable String symbol) {
        List<StockPrice> prices = stockPriceService.getHistoricalPrices(symbol);
        return ResponseEntity.ok(prices);
    }

    /**
     * Endpoint to fetch the real-time quote of a single stock.
     */
    @GetMapping("/{symbol}/realtime")
    public ResponseEntity<StockPrice> getRealtimeQuote(@PathVariable String symbol) {
        StockPrice price = stockPriceService.getRealtimeQuote(symbol);
        return ResponseEntity.ok(price);
    }
}
