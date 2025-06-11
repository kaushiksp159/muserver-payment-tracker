package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentValidationExceptionTest {
    @Test
    void exceptionMessageIsCorrect() {
        String errorMessage = "Test error message";
        PaymentValidationException exception = new PaymentValidationException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}