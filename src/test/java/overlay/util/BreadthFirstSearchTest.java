package overlay.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

// TODO refactor to parametrized test
public class BreadthFirstSearchTest {

    @Test
    public void testGetShortestDistancePath1() {
        int[][] adj = {
                {0, 1, 0, 1, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 0}};
        int src = 0;
        int dest = 7;
        int vertices = 8;
        List<Integer> expected = Arrays.asList(0, 3, 7);
        List<Integer> result = BreadthFirstSearch.getShortestDistance(adj, vertices, src, dest);

        assertEquals(expected, result);
    }

    @Test
    public void testGetShortestDistancePath2() {
        int[][] adj = {
                {0, 1, 0, 1, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 1, 0, 1, 1, 1},
                {0, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0, 1, 0}};
        int src = 2;
        int dest = 6;
        int vertices = 8;
        List<Integer> expected = Arrays.asList(2, 1, 0, 3, 4, 6);
        List<Integer> result = BreadthFirstSearch.getShortestDistance(adj, vertices, src, dest);

        assertEquals(expected, result);
    }
}