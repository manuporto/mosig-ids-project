package overlay.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


/**
 * This class is used when a message is received externally
 * to handle the sending of the message to the right destination.
 * It will add the message to the ExternalMessage queue.
 */
public class SendMessageHandler implements HttpHandler {

    private final Logger logger = LoggerFactory.getLogger(SendMessageHandler.class);
    private BlockingQueue<ExternalMessage> messages;

    SendMessageHandler(BlockingQueue<ExternalMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonMap = mapper.readValue(is, new TypeReference<Map<String, String>>() {});

        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            logger.debug("Destination = " + entry.getKey() + ", Value = " + entry.getValue());
            ExternalMessage message = new ExternalMessage(Integer.parseInt(entry.getKey()), entry.getValue());
            try {
                messages.put(message);
            } catch (InterruptedException e) {
                logger.trace("SendMessageHandler got interrupted when trying to put a message into the externalMessages queue.");
                String response ="Node stopped, couldn't process message.";
                exchange.sendResponseHeaders(500, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                break;
            }
        }
        is.close();
        String response = "Message correctly sent";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        logger.trace("HTTP request handled.");
    }
}
