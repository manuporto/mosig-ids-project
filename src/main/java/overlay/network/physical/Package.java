package overlay.network.physical;

import overlay.network.virtual.Message;

import java.io.Serializable;

/**
 * Class describing the data structure of the packages that are sent.
 */

public class Package implements Serializable {
    private int src;
    private int dest;
    private int nextHop;
    private Message message;

    /**
     * Class constructor to initialize the package.
     * @param src is the physical source ID.
     * @param dest is the physical destination ID.
     * @param nextHop is ID of the next hop.
     * @param message is the actual message.
     */
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