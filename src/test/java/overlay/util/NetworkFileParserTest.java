package overlay.util;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NetworkFileParserTest {

    @Test
    public void parseNetworkInfoFile() throws IOException, URISyntaxException {
        String expectedHost = "localhost";
        int expectedPort = 5672;
        Map<Integer, Integer> expectedTags = Map.of(0, 0,1, 1);
        List<List<Integer>> expectedTopology = Arrays.asList(
                Arrays.asList(0, 1),
                Arrays.asList(1, 0)
        );

        String path = Paths.get(getClass().getResource("/simpleNetwork.json").toURI()).toString();
        NetworkFileParser networkFileParser = new NetworkFileParser(path);

        assertEquals(expectedHost, networkFileParser.getHost());
        assertEquals(expectedPort, networkFileParser.getPort());
        assertEquals(expectedTags, networkFileParser.getTags());
        assertEquals(expectedTopology, networkFileParser.getPhysicalTopology());
        assertEquals(expectedTopology, networkFileParser.getVirtualTopology());
    }
}