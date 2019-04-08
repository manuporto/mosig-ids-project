package overlay.external;

/**
 * This class represents the data structure of the
 * of the messages received externally
 */
public class ExternalMessage {
    private int dest;
    private String message;

    public ExternalMessage(int dest, String message) {
        this.dest = dest;
        this.message = message;
    }

    public int getDest() {
        return dest;
    }

    public String getMessage() {
        return message;
    }
}
