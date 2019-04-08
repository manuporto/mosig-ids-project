package overlay.network.physical;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Class that plays the role of a wrapper for RabbitMQ
 */
class Driver {
    private final Logger logger = LoggerFactory.getLogger(Driver.class);
    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;

    private final int myID;
    private Map<Integer, String> connections;
    private final String connectPrefix = "connnection";
    private final String connectSeparator = "_";

    /**
     * Class constructor to initialize the our environment.
     * @param host is our server host.
     * @param port is the port used.
     * @param exchangeName is the exchange name.
     * @param myID is the Physical ID.
     * @throws IOException
     * @throws TimeoutException
     */
    Driver(String host, int port, String exchangeName, int myID) throws IOException, TimeoutException {
        this.myID = myID;
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

    /**
     * Method to generate a routing key.
     * @param src is the source ID.
     * @param dest is the destination ID.
     * @return a string representing the connection key.
     */
    private String generateRoutingKey(int src, int dest) {
        return connectPrefix + connectSeparator +
                src +
                connectSeparator +
                dest;
    }

    /**
     * Method to add incoming connections.
     * @param src is the source ID.
     * @param deliverCallback Callback interface to be notified when a message is delivered.
     * @throws IOException
     */
    void addIncomingConnection(int src, DeliverCallback deliverCallback) throws IOException {
        channel.queueBind(queueName, exchangeName, generateRoutingKey(src, myID));
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    /**
     * Method to add Outgoing connections to the list of connections.
     * @param dest is the destination ID.
     */
    void addOutgoingConnection(int dest) {
        connections.put(dest, generateRoutingKey(myID, dest));
    }

    /**
     * Method to publish the package.
     * @param aPackage is Package containing the source ID,
     *                 destination ID , message and Next hop.
     * @throws IOException
     */
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

    /**
     * Method to close the channel and the connection.
     */
    void stop() {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            logger.warn("Couldn't close porperly the channel and the connection: " + e.getMessage());
        }

    }
}
