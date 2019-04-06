package overlay.network.physical;

import overlay.network.virtual.Message;

import java.io.Serializable;

public class Package implements Serializable {
    private int src;
    private int dest;
    private int nextHop;
    private Message message;

    Package(int src, int dest, int nextHop, Message message) {
        this.src = src;
        this.dest = dest;
        this.nextHop = nextHop;
        this.message = message;
    }

    public int getSrc() {
        return src;
    }

    int getDest() {
        return dest;
    }

    int getNextHop() {
        return nextHop;
    }

    Message getMessage() {
        return message;
    }

    void setNextHop(int newNextHop) {
        nextHop = newNextHop;
    }

    @Override
    public String toString() {
        return "Package{" +
                "src=" + src +
                ", dest=" + dest +
                ", nextHop=" + nextHop +
                ", message=" + message.toString() +
                '}';
    }
}