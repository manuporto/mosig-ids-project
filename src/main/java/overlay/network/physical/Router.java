package overlay.network.physical;

import com.rabbitmq.client.DeliverCallback;
import overlay.util.BreadthFirstSearch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Router {
    private final int myID;
    private Map<Integer, Integer> nextHopsForDestinations;
    private Driver driver;
    private DeliverCallback deliverCallback;

    public Router(String host, int port, String exchangeName) throws IOException, TimeoutException {
        // TODO use real info
        myID = -1;
        int[][] adj = {{}};
        int vertices = 0;
        nextHopsForDestinations = BreadthFirstSearch.calculateNextHops(myID, adj, vertices);
        driver = new Driver(host, port, exchangeName);
        deliverCallback = (consumerTag, delivery) -> {
            ByteArrayInputStream is = new ByteArrayInputStream(delivery.getBody());
            try (ObjectInputStream ois = new ObjectInputStream(is)) {
                Package pkg = (Package) ois.readObject();
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + pkg.getSrc() + "'");
                // if not dest -> reroute pkg (send(pkg))
                // if dest -> incomingMessages.put(pkg.getMessage())
            } catch (ClassNotFoundException e) {
                // TODO log error
                e.printStackTrace();
            }
        };
    }

    private void processMessages() {
        while (!Thread.interrupted()) {
            // Message msg = outgoingMessages.remove();
            // Package pkg = new Package(pSrc, pDest, msg);
            // send(pkg);
        }
    }

    public void connect(String connectionName) throws IOException {
        driver.addConnection(connectionName, deliverCallback);
    }

    public void send(Package aPackage) throws IOException {
        // TODO send physical next hop info to the driver
        // driver.send(aPackage, nextHopsForDestinations.get(aPackage.getDest()));
        driver.send(aPackage);
    }

}