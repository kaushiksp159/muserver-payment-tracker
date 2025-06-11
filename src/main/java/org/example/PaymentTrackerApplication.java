package org.example;

public class PaymentTrackerApplication {
    public static void main(String[] args) {
        PaymentServer server = new PaymentServer(8080);
        server.start();

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
