package co.quest.xms.valuation.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GlobalQuoteData {

    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GlobalQuote {
        @JsonProperty("01. symbol")
        private String symbol;

        @JsonProperty("02. open")
        private double open;

        @JsonProperty("03. high")
        private double high;

        @JsonProperty("04. low")
        private double low;

        @JsonProperty("05. price")
        private double price;

        @JsonProperty("06. volume")
        private long volume;
    }
}
