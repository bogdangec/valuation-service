package co.quest.xms.valuation.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "stock_prices")
public class StockPrice {
    private String symbol;
    private LocalDateTime timestamp;
    private double open;
    private double close;
    private double high;
    private double low;
    private long volume;
}
