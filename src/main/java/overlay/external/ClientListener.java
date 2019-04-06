package overlay.external;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;

public class ClientListener {

    private final Logger logger = LoggerFactory.getLogger(ClientListener.class);
    private HttpServer server;

    public ClientListener(BlockingQueue<ExternalMessage> messages) throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        logger.info("HTTP server running in: " + server.getAddress().getPort());
        server.createContext("/sendmsg", new SendMessageHandler(messages));
        server.setExecutor(null); // creates a default executor
    }

    public void start() {
        logger.trace("Starting HTTP server...");
        server.start();
        logger.trace("HTTP started.");
    }

    public void stop() {
        logger.trace("Stopping HTTP server...");
        server.stop(0);
        logger.trace("HTTP server stopped.");
    }
}

