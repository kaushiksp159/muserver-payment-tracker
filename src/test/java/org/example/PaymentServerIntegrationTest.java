package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class PaymentServerIntegrationTest {
    @Test
    void serverStartsAndHandlesRequests() throws Exception {
        PaymentServer server = new PaymentServer(8081);
        server.start();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/payment/USD/100"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertTrue(response.body().contains("Payment recorded: USD 100"));
        } finally {
            server.stop();
        }
    }
}