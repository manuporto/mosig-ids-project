package overlay.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class SendMessageHandler implements HttpHandler {

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
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            // TODO return http error message if it's not possible to parse int
            ExternalMessage message = new ExternalMessage(Integer.parseInt(entry.getKey()), entry.getValue());
            try {
                messages.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
                // TODO respond with an error code so the user knows his message couldn't be send
                break;
            }
        }
        is.close();
        String response = "This is the response";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println("Response sent");
    }
}
