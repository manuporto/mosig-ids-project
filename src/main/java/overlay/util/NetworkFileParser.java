package overlay.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class Is a File Parser to get the network information from Json files
 */

public class NetworkFileParser {
    private final Logger logger = LoggerFactory.getLogger(NetworkFileParser.class);
    private String host;
    private int port;
    private Map<Integer, Integer> tags;
    private List<List<Integer>> physicalTopology;
    private List<List<Integer>> virtualTopology;

    /**
     * Class constructor to Parse the file
     * @param path is the location of the file to be parsed
     * @throws IOException
     */
    public NetworkFileParser(String path) throws IOException {
        Path file = Paths.get(path);
        try(InputStream is = Files.newInputStream(file)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(is);
            host = node.get("host").asText();
            port = node.get("port").asInt();
            tags = parseTags(mapper, node.get("tags"));
            physicalTopology = parseTopologyMatrix(mapper, node.get("physicalTopology"));
            virtualTopology = parseTopologyMatrix(mapper, node.get("virtualTopology"));
        } catch (IOException e) {
            logger.error("Error when trying to parse network info file: " + e.getMessage());
            throw e;
        }
    }

    private Map<Integer, Integer> parseTags(ObjectMapper mapper, JsonNode node) throws IOException {
        Map<String, Integer> rawTags = mapper.readValue(node.traverse(), Map.class);
        Map<Integer, Integer> tags = new HashMap<>();
        for (Map.Entry<String, Integer> entry : rawTags.entrySet()) {
            tags.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return tags;
    }

    private List<List<Integer>> parseTopologyMatrix(ObjectMapper mapper, JsonNode node) throws IOException {
        List<List<Integer>> topology = new ArrayList<>();
        int[][] rawTopology = mapper.readValue(node.traverse(), int[][].class);
        for (int[] ints : rawTopology) {
            List<Integer> row = new ArrayList<>();
            for (int anInt : ints) {
                row.add(anInt);
            }
            topology.add(row);
        }
        return topology;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Map<Integer, Integer> getTags() {
        return tags;
    }

    public List<List<Integer>> getPhysicalTopology() {
        return physicalTopology;
    }

    public List<List<Integer>> getVirtualTopology() {
        return virtualTopology;
    }
}
