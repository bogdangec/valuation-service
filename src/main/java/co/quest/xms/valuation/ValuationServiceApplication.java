package co.quest.xms.valuation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ValuationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValuationServiceApplication.class, args);
    }

}
