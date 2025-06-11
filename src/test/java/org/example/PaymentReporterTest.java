package org.example;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

class PaymentReporterTest {
    private PaymentReporter reporter;
    private Map<String, AtomicLong> payments;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        payments = new ConcurrentHashMap<>();
        reporter = new PaymentReporter(payments);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void printSummary_withNoPayments() {
        reporter.printSummary();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("No payments recorded yet."));
    }

    @Test
    void printSummary_withPayments() {
        payments.put("USD", new AtomicLong(100));
        payments.put("EUR", new AtomicLong(200));

        reporter.printSummary();
        String output = outputStream.toString();

        Assertions.assertTrue(output.contains("USD 100"));
        Assertions.assertTrue(output.contains("EUR 200"));
    }
}