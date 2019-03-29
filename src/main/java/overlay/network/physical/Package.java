package overlay.network.physical;

import overlay.network.virtual.Message;

import java.io.Serializable;

public class Package implements Serializable {
    String src;
    String dest;
    Message message;

    public Package(String src, String dest, Message message) {
        this.src = src;
        this.dest = dest;
        this.message = message;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public Message getMessage() {
        return message;
    }
}