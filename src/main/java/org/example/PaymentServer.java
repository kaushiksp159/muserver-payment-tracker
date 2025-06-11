package org.example;

import io.muserver.*;

public class PaymentServer {
// This class is responsible for starting the MuServer and handling payment requests.
    private final int port;
    private final PaymentService paymentService;
    private MuServer server;

    public PaymentServer(int port) {
        this.port = port;
        this.paymentService = new PaymentService();
    }

    public void start() {
        server = MuServerBuilder.httpServer()
                .withHttpPort(port)
                .addHandler(Method.POST, "/payment/{CUR}/{amount}",
                        (request, response, pathParams) -> paymentService.handlePayment(pathParams, response))
                .start();

        System.out.println("Server started at: " + server.uri());
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
        paymentService.shutdown();
    }
}