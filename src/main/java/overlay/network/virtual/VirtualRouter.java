package overlay.network.virtual;

import overlay.network.physical.Router;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class VirtualRouter {
    private Router router;

    public VirtualRouter() throws IOException, TimeoutException {
        router = new Router("", 1, "");
    }

    public void connect(int virtualDest) throws IOException {
        //TODO get physical destination and add it to the router
        String connection = "connection_MyNode_DestNode";
        router.connect(connection);
    }

    public void sendMessage(Message message) {
        String virtualDest = message.getDest();
        // TODO get physical dest
        //int physicalDest = networkInfo.getPhysicalAddr(virtualDest);

    }
}
