package org.example;

import io.muserver.*;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentServer {

    private static final Map<String, AtomicLong> payments = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {

        // Start scheduled task to print payment summary every 60 seconds
        scheduler.scheduleAtFixedRate(PaymentServer::printPayments, 1, 60, TimeUnit.SECONDS);

        // Create and start MuServer
        MuServer server = MuServerBuilder.httpServer().addHandler(Method.POST, "/payment/",(request, response, pathParams) -> {
                    if (request.uri().getPath().startsWith("/payment")) {
                        handlePaymentRequest(String.valueOf(request.uri()), response);
                    } else {
                        response.status(404);
                        response.write("Endpoint not found.");
                    }
                })
                .start();

        System.out.println("Server started at: " + server.uri());
    }

    private static void handlePaymentRequest(String uri, MuResponse response) {
        // Expected format: /payment/<CUR>/<AMOUNT>
        String[] parts = uri.split("/");
        if (parts.length != 4) {
            response.status(400);
            response.write("Invalid URI. Expected format: /payment/<CUR>/<AMOUNT>");
            return;
        }

        String currency = parts[2].toUpperCase();
        long amount;

        try {
            amount = Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            response.status(400);
            response.write("Amount must be a number.");
            return;
        }

        payments.computeIfAbsent(currency, k -> new AtomicLong()).addAndGet(amount);
        response.status(200);
        response.write("Payment recorded: " + currency + " " + amount);
    }

    private static void printPayments() {
        System.out.println("----- Payment Summary -----");
        if (payments.isEmpty()) {
            System.out.println("No payments recorded yet.");
        } else {
            payments.forEach((currency, total) -> {
                long value = total.get();
                if (value != 0) {
                    System.out.println(currency + " " + value);
                }
            });
        }
        System.out.println("---------------------------");
    }
}
