package overlay.network.virtual;

import java.io.Serializable;

public class Message implements Serializable {
    private int src;
    private int dest;
    private int nextHop;
    private String payload;

    public Message(int src, int dest, int nextHop, String payload) {
        this.src = src;
        this.dest = dest;
        this.nextHop = nextHop;
        this.payload = payload;
    }

    public int getNextHop() {
        return nextHop;
    }

    public void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public String getPayload() {
        return payload;
    }
}
