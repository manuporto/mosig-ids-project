package overlay.network.virtual;

import java.io.Serializable;

public class Message implements Serializable {
    private String src;
    private String dest;
    private String payload;

    public Message(String src, String dest, String payload) {
        this.src = src;
        this.dest = dest;
        this.payload = payload;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public String getPayload() {
        return payload;
    }
}
