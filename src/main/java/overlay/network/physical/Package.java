package overlay.network.physical;

import overlay.network.virtual.Message;

import java.io.Serializable;

public class Package implements Serializable {
    int src;
    int dest;
    int nextHop;
    Message message;

    public Package(int src, int dest, int nextHop, Message message) {
        this.src = src;
        this.dest = dest;
        this.nextHop = nextHop;
        this.message = message;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public int getNextHop() {
        return nextHop;
    }

    public Message getMessage() {
        return message;
    }

    public void setNextHop(int newNextHop) {
        nextHop = newNextHop;
    }
}