package org.example;

import io.muserver.MuResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class PaymentServiceTest {
    private PaymentService paymentService;
    private MuResponse mockResponse;
    private Map<String, String> pathParams;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
        mockResponse = Mockito.mock(MuResponse.class);
        pathParams = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
        paymentService.shutdown();
    }

    @Test
    void handlePayment_validInput() {
        pathParams.put("CUR", "USD");
        pathParams.put("amount", "100");

        paymentService.handlePayment(pathParams, mockResponse);

        Mockito.verify(mockResponse).status(200);
        Mockito.verify(mockResponse).write("Payment recorded: USD 100");
    }

    @Test
    void handlePayment_invalidCurrency() {
        pathParams.put("CUR", "INVALID");
        pathParams.put("amount", "100");

        paymentService.handlePayment(pathParams, mockResponse);

        Mockito.verify(mockResponse).status(400);
        Mockito.verify(mockResponse).write(Mockito.contains("Invalid currency code"));
    }

    @Test
    void handlePayment_invalidAmount() {
        pathParams.put("CUR", "USD");
        pathParams.put("amount", "invalid");

        paymentService.handlePayment(pathParams, mockResponse);

        Mockito.verify(mockResponse).status(400);
        Mockito.verify(mockResponse).write("Invalid amount format");
    }

    @Test
    void handlePayment_zeroAmount() {
        pathParams.put("CUR", "USD");
        pathParams.put("amount", "0");

        paymentService.handlePayment(pathParams, mockResponse);

        Mockito.verify(mockResponse).status(400);
        Mockito.verify(mockResponse).write("Amount must not be zero");
    }
}