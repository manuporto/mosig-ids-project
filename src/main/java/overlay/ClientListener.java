package overlay;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import overlay.network.Router;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ClientListener {

    private HttpServer server;

    public ClientListener() throws IOException, TimeoutException {
        Router router = new Router("localhost", 5672, "defaultExchange");
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/sendmsg", new MyHandler());
        server.setExecutor(null); // creates a default executor
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStream is = t.getRequestBody();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jsonMap = mapper.readValue(is, new TypeReference<Map<String, String>>() {});

            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
            is.close();
        }
    }

}

