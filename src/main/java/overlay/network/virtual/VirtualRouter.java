package overlay.network.virtual;

import overlay.external.ExternalMessage;
import overlay.util.BreadthFirstSearch;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VirtualRouter implements Runnable {
    private final int myID;
    private Map<Integer, Integer> nextHopsForDestinations;
    private ConcurrentLinkedQueue<ExternalMessage> externalMessages;
    private ConcurrentLinkedQueue<Message> incomingMessages;
    private ConcurrentLinkedQueue<Message> outgoingMessages;

    public VirtualRouter(ConcurrentLinkedQueue<ExternalMessage> externalMessages,
                         ConcurrentLinkedQueue<Message> incomingMessages,
                         ConcurrentLinkedQueue<Message> outgoingMessages) {
        this.myID = -1;
        int[][] adj = {{}}; // TODO get actual matrix info from somewhere
        this.nextHopsForDestinations = BreadthFirstSearch.calculateNextHops(myID, adj, 0);
        this.externalMessages = externalMessages;
        this.incomingMessages = incomingMessages;
        this.outgoingMessages = outgoingMessages;
    }

    private void listenForExternalMessages() {
        while(!Thread.currentThread().isInterrupted()) {
            ExternalMessage externalMessage = externalMessages.remove();
            int dest = externalMessage.getDest();
            Message newMessage = new Message(myID, dest, nextHopsForDestinations.get(dest), externalMessage.getMessage());
            outgoingMessages.add(newMessage);
        }
    }

    private void listenForIncomingMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            Message incomingMessage = incomingMessages.remove();
            if (incomingMessage.getDest() == myID) { // TODO get my node id
                // TODO send to messagePrinter class
                System.out.println(incomingMessage.toString());
            } else {
                int nextHop = nextHopsForDestinations.get(incomingMessage.getDest());
                incomingMessage.setNextHop(nextHop);
                outgoingMessages.add(incomingMessage);
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
