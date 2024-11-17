# Valuation Service Backend
The Valuation Service Backend is a Spring Boot application designed to provide reliable access to financial data, including historical stock prices and real-time quotes. It integrates with MongoDB for data storage and Kafka for event streaming, supporting both free and premium tiers.

## Features
- Fetch historical stock prices via REST API.
- Retrieve real-time stock quotes.
- Store and query data using MongoDB.
- Publish updates to Kafka for event-driven systems.
- Freemium API model with premium-tier features.

## Prerequisites
- Java 21
- Docker and Docker Compose
- MongoDB Atlas cluster or local MongoDB setup
- Kafka (Confluent Cloud or local Kafka setup)
- Alpha Vantage API key

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/bogdangec/valuation-service.git    
cd valuation-service-backend
```

### Clone the Repository
Run Locally
```
./gradlew bootRun
```

---

## API Endpoints

### Fetch Historical Stock Prices
```
GET /api/v1/stock-prices/{symbol}
```
- **Description**: Retrieves historical prices for a stock symbol.
- **Response**: List of `StockPrice` objects.

### Fetch Real-Time Stock Quote
```
GET /api/v1/stock-prices/{symbol}/realtime
```
- **Description**: Retrieves the real-time quote for a stock symbol.
- **Response**: A `StockPrice` object.

Example:
```bash
curl -X GET "http://localhost:8080/api/v1/stock-prices/AAPL" -H "accept: application/json"
```

---

## Configuration
Add the following to your `application.properties` file:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/<db_name>

# Kafka Configuration
client.id=<kafka_client_id>
spring.kafka.bootstrap-servers=<kafka_bootstrap_server>
spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='<API_KEY>' password='<API_SECRET>';
spring.kafka.properties.sasl.mechanism=PLAIN

kafka.topic.stock-prices=<kafka_topic_name>

# Alpha Vantage Configuration
alpha-vantage.api.key=<your_api_key>
```
