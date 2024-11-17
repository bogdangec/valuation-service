package co.quest.xms.valuation.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSeriesData {

    // Maps the "Time Series (Daily)" field in the response
    @JsonProperty("Time Series (Daily)")
    private Map<String, DailyData> timeSeries;

    /**
     * Inner class to map each entry of the daily time series.
     */
    @Data
    public static class DailyData {

        @JsonProperty("1. open")
        private double open;

        @JsonProperty("2. high")
        private double high;

        @JsonProperty("3. low")
        private double low;

        @JsonProperty("4. close")
        private double close;

        @JsonProperty("5. volume")
        private long volume;
    }
}
