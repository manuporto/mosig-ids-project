package overlay.network.virtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overlay.external.ExternalMessage;
import overlay.network.NetworkInfo;
import overlay.util.BreadthFirstSearch;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Class for the Virtual router that will communicate with the router through 2 queues;
 * incoming messages queue and outgoing messages queue.
 */

public class VirtualRouter implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(VirtualRouter.class);
    private NetworkInfo networkInfo;
    private final int myID;
    private Map<Integer, Integer> nextHopsForDestinations;
    private BlockingQueue<ExternalMessage> externalMessages;
    private BlockingQueue<Message> incomingMessages;
    private BlockingQueue<Message> outgoingMessages;

    /**
     * Class constructor to initialize the virtual router.
     * @param networkInfo includes the information of the network.
     * @param externalMessages is the external message queue.
     * @param incomingMessages is the queue of messages coming from the router.
     * @param outgoingMessages is the queue of messages going to the router.
     */
    public VirtualRouter(NetworkInfo networkInfo, BlockingQueue<ExternalMessage> externalMessages,
                         BlockingQueue<Message> incomingMessages,
                         BlockingQueue<Message> outgoingMessages) {
        this.networkInfo = networkInfo;
        this.myID = networkInfo.getVirtualID();
        this.nextHopsForDestinations = BreadthFirstSearch.calculateNextHops(myID, networkInfo.getvTopology());
        this.externalMessages = externalMessages;
        this.incomingMessages = incomingMessages;
        this.outgoingMessages = outgoingMessages;
    }

    /**
     * Running thread that will listen to external messages.
     */

    private void listenForExternalMessages() {
        while(!Thread.currentThread().isInterrupted()) {
            ExternalMessage externalMessage;
            try {
                externalMessage = externalMessages.take();
                int dest = externalMessage.getDest();
                Message newMessage = new Message(myID, dest, nextHopsForDestinations.get(dest), externalMessage.getMessage());
                outgoingMessages.put(newMessage);
                logger.debug("Sent external message: " + newMessage.toString());
            } catch (InterruptedException e) {
                logger.trace("VirtualRouter got interrupted when trying to get a message from the externalMessages queue.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Running thread that will listen for Incoming messages.
     */
    
    private void listenForIncomingMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message incomingMessage = incomingMessages.take();
                if (incomingMessage.getDest() == myID) {
                    logger.info("Received message: " + incomingMessage.toString());
                } else {
                    int nextHop = nextHopsForDestinations.get(incomingMessage.getDest());
                    incomingMessage.setNextHop(nextHop);
                    outgoingMessages.put(incomingMessage);
                    logger.debug("Received and rerouted following message: " + incomingMessage.toString());
                }
            } catch (InterruptedException e) {
                logger.trace("VirtualRouter got interrupted when trying to get a message from the incomingMessages queue.");
                Thread.currentThread().interrupt();
            }

        }
    }

    @Override
    public void run() {
        Thread externalMessagesListener = new Thread(this::listenForExternalMessages);
        externalMessagesListener.start();
        listenForIncomingMessages();

        externalMessagesListener.interrupt();
        try {
            externalMessagesListener.join();
        } catch (InterruptedException e) {
            logger.warn("Couldn't properly join the externalMessagesListener thread: " + e.getMessage());
        }
    }
}
