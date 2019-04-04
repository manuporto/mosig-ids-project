package overlay.network.physical;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

class Driver {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;

    private final int myID;
    private Map<Integer, String> connections;
    private final String connectPrefix = "connnection";
    private final String connectSeparator = "_";

    Driver(String host, int port, String exchangeName) throws IOException, TimeoutException {
        myID = -1;
        this.exchangeName = exchangeName;
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        this.connection = factory.newConnection();
        factory.setAutomaticRecoveryEnabled(true);
        this.channel = connection.createChannel();
        this.channel.exchangeDeclare(exchangeName, "direct");
        queueName = this.channel.queueDeclare().getQueue();
        connections = new HashMap<>();
    }

    private String generateRoutingKey(int src, int dest) {
        return connectPrefix + connectSeparator +
                src +
                connectSeparator +
                dest;
    }

    void addIncomingConnection(int src, DeliverCallback deliverCallback) throws IOException {
        channel.queueBind(queueName, exchangeName, generateRoutingKey(src, myID));
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    void addOutgoingConnection(int dest) {
        connections.put(dest, generateRoutingKey(myID, dest));
    }

    void send(Package aPackage) throws IOException {
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

    void stop() {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("Couldn't close porperly the channel and the connection");
        }

    }
}
