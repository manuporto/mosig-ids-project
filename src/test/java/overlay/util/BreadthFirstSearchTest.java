package overlay.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

// TODO refactor to parametrized test
public class BreadthFirstSearchTest {

    @Test
    public void testGetShortestDistancePath1() {
        List<List<Integer>> adj = Arrays.asList(
                Arrays.asList(0, 1, 0, 1, 0, 0, 0, 0),
                Arrays.asList(1, 0, 1, 0, 0, 0, 0, 0),
                Arrays.asList(0, 1, 0, 0, 0, 0, 0, 0),
                Arrays.asList(1, 0, 0, 0, 1, 0, 0, 1),
                Arrays.asList(0, 0, 0, 1, 0, 1, 1, 1),
                Arrays.asList(0, 0, 0, 0, 1, 0, 1, 0),
                Arrays.asList(0, 0, 0, 0, 0, 1, 0, 1),
                Arrays.asList(0, 0, 0, 1, 1, 0, 1, 0)
        );

        int src = 0;
        int dest = 7;
        int vertices = 8;
        List<Integer> expected = Arrays.asList(0, 3, 7);
        List<Integer> result = BreadthFirstSearch.getShortestDistance(adj, vertices, src, dest);

        assertEquals(expected, result);
    }

    @Test
    public void testGetShortestDistancePath2() {
        List<List<Integer>> adj = Arrays.asList(
                Arrays.asList(0, 1, 0, 1, 0, 0, 0, 0),
                Arrays.asList(1, 0, 1, 0, 0, 0, 0, 0),
                Arrays.asList(0, 1, 0, 0, 0, 0, 0, 0),
                Arrays.asList(1, 0, 0, 0, 1, 0, 0, 1),
                Arrays.asList(0, 0, 0, 1, 0, 1, 1, 1),
                Arrays.asList(0, 0, 0, 0, 1, 0, 1, 0),
                Arrays.asList(0, 0, 0, 0, 0, 1, 0, 1),
                Arrays.asList(0, 0, 0, 1, 1, 0, 1, 0)
        );
        int src = 2;
        int dest = 6;
        int vertices = 8;
        List<Integer> expected = Arrays.asList(2, 1, 0, 3, 4, 6);
        List<Integer> result = BreadthFirstSearch.getShortestDistance(adj, vertices, src, dest);

        assertEquals(expected, result);
    }
}