package overlay.network.virtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import overlay.external.ExternalMessage;
import overlay.network.NetworkInfo;
import overlay.util.BreadthFirstSearch;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class VirtualRouter implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(VirtualRouter.class);
    private NetworkInfo networkInfo;
    private final int myID;
    private Map<Integer, Integer> nextHopsForDestinations;
    private BlockingQueue<ExternalMessage> externalMessages;
    private BlockingQueue<Message> incomingMessages;
    private BlockingQueue<Message> outgoingMessages;

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

    private void listenForExternalMessages() {
        while(!Thread.currentThread().isInterrupted()) {
            ExternalMessage externalMessage;
            try {
                externalMessage = externalMessages.take();
                int dest = externalMessage.getDest();
                Message newMessage = new Message(myID, dest, nextHopsForDestinations.get(dest), externalMessage.getMessage());
                outgoingMessages.put(newMessage);
            } catch (InterruptedException e) {
                logger.trace("VirtualRouter got interrupted when trying to get a message from the externalMessages queue.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void listenForIncomingMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message incomingMessage = incomingMessages.take();
                if (incomingMessage.getDest() == myID) {
                    logger.info("Received message from " + incomingMessage.getSrc() + " to " +
                            incomingMessage.getDest() + " with the following message: " + incomingMessage.getPayload());
                } else {
                    int nextHop = nextHopsForDestinations.get(incomingMessage.getDest());
                    incomingMessage.setNextHop(nextHop);
                    outgoingMessages.put(incomingMessage);
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
