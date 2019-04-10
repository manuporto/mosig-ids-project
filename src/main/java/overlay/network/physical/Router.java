package overlay.network.physical;

import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overlay.network.NetworkInfo;
import overlay.network.virtual.Message;
import overlay.util.BreadthFirstSearch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Running thread that play the the role of the physical router
 */
public class Router implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Router.class);
    private NetworkInfo networkInfo;
    private final int myID;
    private Map<Integer, Integer> nextHopsForDestinations;
    private BlockingQueue<Message> incomingMessages;
    private BlockingQueue<Message> outgoingMessages;
    private Driver driver;
    private DeliverCallback deliverCallback;

    /**
     * Class constructor to initialize the physical router.
     * @param networkInfo (Host,port,Tags,virtual and physical topologies)
     * @param incomingMessages (Queue where the package will be sent to after routing.
     *                          To be handled by the virtual router later on)
     * @param outgoingMessages (Queue where the message will be taken from .
     *                         coming from the virtual router)
     * @throws IOException
     * @throws TimeoutException
     */
    public Router(NetworkInfo networkInfo, BlockingQueue<Message> incomingMessages,
                  BlockingQueue<Message> outgoingMessages) throws IOException, TimeoutException {
        this.networkInfo = networkInfo;
        myID = networkInfo.getPhysicalID();
        nextHopsForDestinations = BreadthFirstSearch.calculateNextHops(myID, networkInfo.getpTopology());

        this.incomingMessages = incomingMessages;
        this.outgoingMessages = outgoingMessages;

        driver = new Driver(networkInfo.getHost(), networkInfo.getPort(), networkInfo.getExchangeName(), myID);
        deliverCallback = (consumerTag, delivery) -> {
            ByteArrayInputStream is = new ByteArrayInputStream(delivery.getBody());
            try (ObjectInputStream ois = new ObjectInputStream(is)) {
                Package pkg = (Package) ois.readObject();
                logger.debug("Received from routingKey " +
                        delivery.getEnvelope().getRoutingKey() + " the following package: " + pkg.toString());
                if (pkg.getDest() != myID) {
                    pkg.setNextHop(nextHopsForDestinations.get(pkg.getDest()));
                    driver.send(pkg);
                    logger.debug("Package rerouted: " + pkg.toString());
                } else {
                    this.incomingMessages.put(pkg.getMessage());
                    logger.debug("Package arrived it's destination, sent to the Virtual Router");
                }
            } catch (ClassNotFoundException | InterruptedException e) {
                logger.warn("Error when trying to deserialize package.");
            }
        };

        setConnections();
    }

    /**
     * Method to send a connection request to all adjacent nodes
     * @throws IOException
     */
    private void setConnections() throws IOException {
        List<Integer> adjList = networkInfo.getpTopology().get(myID);
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i) == 1) connect(i);
        }
    }

    /**
     * Method to process the messages received from the OutgoingMessages queue
     */
    private void processMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            Message msg;
            try {
                msg = outgoingMessages.take();
            } catch (InterruptedException e) {
                logger.trace("Router got interrupted when trying to get a message from the outgoingMessages queue.");
                Thread.currentThread().interrupt();
                break;
            }
            int pSrc = networkInfo.translateToPhysical(msg.getSrc());
            int pDest = networkInfo.translateToPhysical(msg.getNextHop());
            int nextHop = nextHopsForDestinations.get(pDest);
            Package pkg = new Package(pSrc, pDest, nextHop, msg);
            try {
                driver.send(pkg);
                logger.debug("Sent package: " + pkg.toString());
            } catch (IOException e) {
                logger.warn("Error when trying to send package: " + e.getMessage());
            }
        }
    }

    /**
     * Method to establish the connection
     * @param dest is the ID of the destination
     * @throws IOException
     */
    private void connect(int dest) throws IOException {
        driver.addIncomingConnection(dest, deliverCallback);
        driver.addOutgoingConnection(dest);
    }

    @Override
    public void run() {
        processMessages();
        logger.debug("Stopping router...");
        driver.stop();
        logger.debug("Router stopped.");

    }
}