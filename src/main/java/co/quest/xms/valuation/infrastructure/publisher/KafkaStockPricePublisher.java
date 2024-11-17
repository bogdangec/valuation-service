package co.quest.xms.valuation.infrastructure.publisher;

import co.quest.xms.valuation.application.publisher.StockPricePublisher;
import co.quest.xms.valuation.domain.model.StockPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaStockPricePublisher implements StockPricePublisher {

    private final KafkaTemplate<String, StockPrice> kafkaTemplate;

    @Value("${kafka.topic.stock-prices}")
    private String topic;

    public KafkaStockPricePublisher(KafkaTemplate<String, StockPrice> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(StockPrice stockPrice) {
        kafkaTemplate.send(topic, stockPrice);
    }
}
