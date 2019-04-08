package overlay.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.w3c.dom.Node;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Class that will parse JSON files containing the needed information about the network.
 */
public class FileParser {
    public static void main(String[] args) {


        File file = new File("res/NetworkInfo.json");


        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });
            NodeInfo nodeInfo = new NodeInfo();

            nodeInfo.setpTag((int) map.get("1"));
            nodeInfo.setvTag((int) map.get("2"));
            nodeInfo.setpTop((int[][]) map.get("PTop"));
            nodeInfo.setvTop((int[][]) map.get("VTop"));

            System.out.println(nodeInfo.getpTag());
            System.out.println(nodeInfo.getvTag());
            System.out.println(nodeInfo.getpTop());
            System.out.println(nodeInfo.getvTop());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
