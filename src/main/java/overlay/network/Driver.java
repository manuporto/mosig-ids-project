package overlay.network;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Driver {

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public Driver(String host, int port) throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            this.connection = connection;
            this.channel = channel;
            // TODO set proper exchange name
            this.channel.exchangeDeclare("exchange-name", "direct");
        }
    }

    public void addConnection(String name) {

    }
}
