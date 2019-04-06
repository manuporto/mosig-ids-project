package overlay.network.virtual;

import java.io.Serializable;

public class Message implements Serializable {
    private int src;
    private int dest;
    private int nextHop;
    private String payload;

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
