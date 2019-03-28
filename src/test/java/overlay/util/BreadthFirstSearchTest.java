package overlay.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BreadthFirstSearchTest {

    @Before
    public void setUp() throws Exception {
        int[][] adj = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 0},
                {1, 0, 0, 0}};
        int[] srcs = {0, 1, 2, 3};
    }

    @Test
    public void testGetShortestDistance() {
        int[][] adj = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 1, 0, 0},
                {1, 0, 0, 0}};
        int src = 0;
        int dest = 1;
        int vertices = 4;
        List<Integer> expected = new ArrayList<>(2);
        expected.add(0);
        expected.add(1);
        //expected.add(2);
        List<Integer> result = BreadthFirstSearch.getShortestDistance(adj, vertices, src, dest);

        assertEquals(expected, result);
    }
}