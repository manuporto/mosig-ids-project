package overlay.external;

public class ExternalMessage {
    private String dest;
    private String message;

    public ExternalMessage(String dest, String message) {
        this.dest = dest;
        this.message = message;
    }

    public String getDest() {
        return dest;
    }

    public String getMessage() {
        return message;
    }
}
