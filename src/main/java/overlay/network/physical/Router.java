package overlay.network.physical;

import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Router {
    private Driver driver;

    private DeliverCallback deliverCallback;

    public Router(String host, int port, String exchangeName) throws IOException, TimeoutException {
        driver = new Driver(host, port, exchangeName);
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
    }

    public void connect(String connectionName) throws IOException {
        driver.addConnection(connectionName, deliverCallback);
    }

    public void send(String destination, String message) throws IOException {
        driver.send(destination, message);
    }

}