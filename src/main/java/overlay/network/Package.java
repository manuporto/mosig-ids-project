package overlay.network;

public class Package {
    String src;
    String dest;
    String msg;

    public Package(String src, String dest, String msg) {
        this.src = src;
        this.dest = dest;
        this.msg = msg;
    }

    public void getMsg() {}
}