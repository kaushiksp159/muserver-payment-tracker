# MuServer Payment Tracker

A simple payment tracking server built with MuServer that records payments in different currencies and provides periodic summaries.

## Features

- RESTful API for recording payments
- Support for all ISO 4217 currency codes
- Real-time payment tracking
- Automatic payment summaries every 60 seconds
- Thread-safe concurrent payment processing
- Graceful shutdown handling

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository using this URL: https://github.com/kaushiksp159/spring-boot-payment-tracker.git
2. Navigate to the project directory:
   cd muserver-payment-tracker
3. Build the project:
   mvn clean package
4. Run the application:
   java -jar target/muserver-payment-tracker-0.0.1-SNAPSHOT.jar

## API Usage 

- Record Payment: POST /payment/{currency}/{amount}
  - This endpoint records a payment with a specified currency and amount.
    - Example: `curl -X POST http://localhost:8080/payment/USD/100`
      USD: Any valid ISO 4217 currency code
      100: Payment amount (must be positive or negative integer but not zero)

## Project Structure

- PaymentServer: Main server configuration and setup
- PaymentService: Core payment processing logic
- PaymentReporter: Periodic payment summary reporting
- PaymentValidationException: Custom exception handling

## Testing 
To run the test suite, use the following command:
```bash
mvn test 