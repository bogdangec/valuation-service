package co.quest.xms.valuation.infrastructure.publisher;

import co.quest.xms.valuation.domain.model.StockPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import static co.quest.xms.valuation.util.TestConstants.MOCK_AAPL_PRICE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafkaStockPricePublisherTest {
    @Mock
    private KafkaTemplate<String, StockPrice> kafkaTemplate;

    @InjectMocks
    private KafkaStockPricePublisher kafkaStockPricePublisher;

    @Value("${kafka.topic.stock-prices}")
    private String topic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublish() {
        // When
        kafkaStockPricePublisher.publish(MOCK_AAPL_PRICE);

        // Then
        verify(kafkaTemplate, times(1)).send(topic, MOCK_AAPL_PRICE);
    }
}
