package overlay.network.virtual;

import overlay.external.ExternalMessage;
import overlay.network.NetworkInfo;
import overlay.util.BreadthFirstSearch;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class VirtualRouter implements Runnable {
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
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void listenForIncomingMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message incomingMessage = incomingMessages.take();
                if (incomingMessage.getDest() == myID) {
                    System.out.println("Received message from " + incomingMessage.getSrc() + " to " +
                            incomingMessage.getDest() + " with the following message: " + incomingMessage.getPayload());
                } else {
                    int nextHop = nextHopsForDestinations.get(incomingMessage.getDest());
                    incomingMessage.setNextHop(nextHop);
                    outgoingMessages.put(incomingMessage);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
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
            System.err.println("Couldn't properly join thread");
            e.printStackTrace();
        }
    }
}
