package overlay.network;

public class Router {
    private Driver driver;

    private DeliverCallback deliverCallback;

    public Router(String host, int port, String exchangeName) {
        driver = Driver(host, port, exchangeName);
        deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    }

    public void connect(String connectionName) {
        driver.addConnection(connectionName, deliverCallback);
    }

    public void send(String destination, String message) {
        driver.send(destination, message);
    }

}