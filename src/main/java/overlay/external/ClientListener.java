package overlay.external;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;

public class ClientListener {

    private HttpServer server;

    public ClientListener(BlockingQueue<ExternalMessage> messages) throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        System.out.println("HTTP server running in: " + server.getAddress().getPort());
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

