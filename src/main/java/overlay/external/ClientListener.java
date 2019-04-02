package overlay.external;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientListener {

    private HttpServer server;

    public ClientListener(ConcurrentLinkedQueue<ExternalMessage> messages) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/sendmsg", new SendMessageHandler(messages));
        server.setExecutor(null); // creates a default executor
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}

