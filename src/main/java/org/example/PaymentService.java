package org.example;

import io.muserver.MuResponse;

import java.util.Map;
import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentService {
    // This class would contain methods to handle payment processing,
    // such as validating the payment, recording it, and logging current payments.
    private final Map<String, AtomicLong> payments;
    private final ScheduledExecutorService scheduler;
    private final PaymentReporter reporter;

    public PaymentService() {
        this.payments = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.reporter = new PaymentReporter(payments);
        startReportingSchedule();
    }

    private void startReportingSchedule() {
        scheduler.scheduleAtFixedRate(reporter::printSummary, 1, 60, TimeUnit.SECONDS);
    }


    public void handlePayment(Map<String, String> pathParams, MuResponse response) {
        try {
            String currency = validateCurrency(pathParams.get("CUR"));
            long amount = validateAmount(pathParams.get("amount"));

            payments.computeIfAbsent(currency, k -> new AtomicLong())
                    .addAndGet(amount);

            response.status(200);
            response.write("Payment recorded: " + currency + " " + amount);
        } catch (PaymentValidationException e) {
            response.status(400);
            response.write(e.getMessage());
        }
    }

    private String validateCurrency(String currency) throws PaymentValidationException {
        if (currency == null || currency.trim().isEmpty()) {
            throw new PaymentValidationException("Currency cannot be empty");
        }
        String upperCurrency = currency.toUpperCase();
        try {
            Currency.getInstance(upperCurrency);
        } catch (IllegalArgumentException e) {
            throw new PaymentValidationException("Invalid currency code: " + currency);
        }

        return upperCurrency;
    }

    private long validateAmount(String amountStr) throws PaymentValidationException {
        try {
            long amount = Long.parseLong(amountStr);
            if (amount == 0) {
                throw new PaymentValidationException("Amount must not be zero");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new PaymentValidationException("Invalid amount format");
        }
    }

    public void shutdown() {
        // Logic to clean up resources if needed
        System.out.println("PaymentService is shutting down.");
    }
}
