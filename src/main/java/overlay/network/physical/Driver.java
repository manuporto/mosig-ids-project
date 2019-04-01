package overlay.network.physical;

import com.rabbitmq.client.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.lang.StringBuilder;

public class Driver {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;

    private final int myID;
    private Map<Integer, String> connections;
    private final String connectPrefix = "connnection";
    private final String connectSeparator = "_";

    public Driver(String host, int port, String exchangeName) throws IOException, TimeoutException {
        myID = -1;
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
        connections = new HashMap<>();
    }

    private String generateRoutingKey(int src, int dest) {
        StringBuilder routingKey = new StringBuilder(connectPrefix);
        routingKey.append(connectSeparator);
        routingKey.append(src);
        routingKey.append(connectSeparator);
        routingKey.append(dest);
        return routingKey.toString();
    }

    public void addIncomingConnection(int src, DeliverCallback deliverCallback) throws IOException {
        channel.queueBind(queueName, exchangeName, generateRoutingKey(src, myID));
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void addOutgoingConnection(int dest) {
        connections.put(dest, generateRoutingKey(myID, dest));
    }

    public void send(Package aPackage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(aPackage);
        channel.basicPublish(
                exchangeName,
                connections.get(aPackage.getNextHop()),
                null,
                os.toByteArray());
        objectOutputStream.close();
    }

}
