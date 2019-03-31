package overlay.network.virtual;

import overlay.external.ExternalMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VirtualRouter implements Runnable {
    private ConcurrentLinkedQueue<ExternalMessage> externalMessages;
    private ConcurrentLinkedQueue<Message> incomingMessages;
    private ConcurrentLinkedQueue<Message> outgoingMessages;

    public VirtualRouter(ConcurrentLinkedQueue<ExternalMessage> externalMessages, ConcurrentLinkedQueue<Message> incomingMessages, ConcurrentLinkedQueue<Message> outgoingMessages) {
        this.externalMessages = externalMessages;
        this.incomingMessages = incomingMessages;
        this.outgoingMessages = outgoingMessages;
    }

    private void listenForExternalMessages() {
        while(!Thread.interrupted()) {
            ExternalMessage externalMessage = externalMessages.remove();
            // TODO get correct ids
            Message newMessage = new Message(-1, externalMessage.getDest(), -1, externalMessage.getMessage());
            outgoingMessages.add(newMessage);
        }
    }

    private void listenForIncomingMessages() {
        while (!Thread.interrupted()) {
            Message incomingMessage = incomingMessages.remove();
            if (incomingMessage.getDest() == -1) { // TODO get my node id
                // TODO send to messagePrinter class
                System.out.println(incomingMessage.toString());
            } else {
                // TODO reroute the message
                outgoingMessages.add(incomingMessage);
            }
        }
    }

    @Override
    public void run() {
        // TODO check if i need to interrupt this thread when the main one (I) gets interrupted
        new Thread(this::listenForExternalMessages).start();
        new Thread(this::listenForIncomingMessages).start();
    }
}
