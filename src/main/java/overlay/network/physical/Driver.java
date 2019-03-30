package overlay.network.physical;

import com.rabbitmq.client.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

    public void send(Package aPackage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(aPackage);
        channel.basicPublish(
                exchangeName,
                aPackage.getDest(), // TODO get actual destination
                null,
                os.toByteArray());
                //message.getBytes(StandardCharsets.UTF_8));
        objectOutputStream.close();
    }

}
