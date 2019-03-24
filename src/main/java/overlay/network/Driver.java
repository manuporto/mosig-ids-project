package overlay.network;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Driver {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;

    public Driver(String host, int port, String exchangeName) throws IOException, TimeoutException {
        this.exchangeName = exchangeName;
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            this.connection = connection;
            this.channel = channel;
            this.channel.exchangeDeclare(exchangeName, "direct");
            queueName = this.channel.queueDeclare().getQueue();
        }
    }

    public void addConnection(String connectionName, DeliverCallback deliverCallback) throws IOException {
        channel.queueBind(queueName, exchangeName, connectionName);
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void send(String destination, String message) throws IOException {
        channel.basicPublish(
                exchangeName,
                destination,
                null,
                message.getBytes(StandardCharsets.UTF_8));
    }

}
