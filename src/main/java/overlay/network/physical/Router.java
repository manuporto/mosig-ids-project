package overlay.network.physical;

import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Router {
    private Driver driver;

    private DeliverCallback deliverCallback;

    public Router(String host, int port, String exchangeName) throws IOException, TimeoutException {
        driver = new Driver(host, port, exchangeName);
        deliverCallback = (consumerTag, delivery) -> {
            ByteArrayInputStream is = new ByteArrayInputStream(delivery.getBody());
            try (ObjectInputStream ois = new ObjectInputStream(is)) {
                Package pkg = (Package) ois.readObject();
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + pkg.getSrc() + "'");
            } catch (ClassNotFoundException e) {
                // TODO log error
                e.printStackTrace();
            }
        };
    }

    public void connect(String connectionName) throws IOException {
        driver.addConnection(connectionName, deliverCallback);
    }

    public void send(Package aPackage) throws IOException {
        driver.send(aPackage);
    }

}