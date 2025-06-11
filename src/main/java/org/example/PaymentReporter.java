package org.example;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentReporter {
// This class is responsible for reporting the payment summary.
    private final Map<String, AtomicLong> payments;

    public PaymentReporter(Map<String, AtomicLong> payments) {
        this.payments = payments;
    }

    public void printSummary() {
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