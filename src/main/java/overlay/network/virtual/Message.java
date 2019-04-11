package overlay.network.virtual;

import java.io.Serializable;

/**
 * Data Structure for Messages interacting in the Virtual network.
 */

public class Message implements Serializable {
    private int src;
    private int dest;
    private int nextHop;
    private String payload;

    /**
     * Class constructor to construct a message
     * @param src is the virtual source ID.
     * @param dest is the virtual destination ID.
     * @param nextHop is the ID of the Next hop.
     * @param payload is the message .
     */
    Message(int src, int dest, int nextHop, String payload) {
        this.src = src;
        this.dest = dest;
        this.nextHop = nextHop;
        this.payload = payload;
    }

    public int getNextHop() {
        return nextHop;
    }

    void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

    public int getSrc() {
        return src;
    }

    int getDest() {
        return dest;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "src=" + src +
                ", dest=" + dest +
                ", nextHop=" + nextHop +
                ", payload='" + payload + '\'' +
                '}';
    }
}
