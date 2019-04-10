package overlay.external;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;

/**
 * This class will create the http server to listen to the client and handle
 * the messages sent by the client
 */

public class ClientListener {

    private final Logger logger = LoggerFactory.getLogger(ClientListener.class);
    private HttpServer server;

    /**
     * Class Constructor to initialize the http server
     * @param messages is the queue the clients will be sending the messages to.
     * @throws IOException
     */
    public ClientListener(BlockingQueue<ExternalMessage> messages) throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        logger.info("HTTP server running in: " + server.getAddress().getPort());
        server.createContext("/sendmsg", new SendMessageHandler(messages));
        server.setExecutor(null); // creates a default executor
    }

    /**
     * Method to start the server
     */

    public void start() {
        logger.trace("Starting HTTP server...");
        server.start();
        logger.trace("HTTP started.");
    }

    /**
     * Method to stop the server
     */

    public void stop() {
        logger.trace("Stopping HTTP server...");
        server.stop(0);
        logger.trace("HTTP server stopped.");
    }
}

