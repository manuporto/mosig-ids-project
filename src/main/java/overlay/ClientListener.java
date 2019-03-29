package overlay;
import com.sun.net.httpserver.HttpServer;
import overlay.network.virtual.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

public class ClientListener {

    private HttpServer server;
    private ConcurrentLinkedQueue<Message> messages;

    public ClientListener(ConcurrentLinkedQueue<Message> messages) throws IOException, TimeoutException {
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

