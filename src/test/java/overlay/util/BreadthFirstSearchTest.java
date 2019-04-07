package overlay.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BreadthFirstSearchTest {
    private List<List<Integer>> adj;
    private int src;
    private int dest;
    private List<Integer> expected;

    @Before
    public void setUp() {
        this.adj = Arrays.asList(
                Arrays.asList(0, 1, 0, 1, 0, 0, 0, 0),
                Arrays.asList(1, 0, 1, 0, 0, 0, 0, 0),
                Arrays.asList(0, 1, 0, 0, 0, 0, 0, 0),
                Arrays.asList(1, 0, 0, 0, 1, 0, 0, 1),
                Arrays.asList(0, 0, 0, 1, 0, 1, 1, 1),
                Arrays.asList(0, 0, 0, 0, 1, 0, 1, 0),
                Arrays.asList(0, 0, 0, 0, 1, 1, 0, 1),
                Arrays.asList(0, 0, 0, 1, 1, 0, 1, 0)
        );
    }

    @Parameterized.Parameters
    public static Collection<Integer[][]> data() {
        return Arrays.asList(new Integer[][][] {
                { {0}, {7}, {0, 3, 7} }, { {2}, {6}, {2, 1, 0, 3, 4, 6} }
        });
    }

    public BreadthFirstSearchTest(Integer[] src, Integer[] dest, Integer[] expected) {
        this.src = src[0];
        this.dest = dest[0];
        this.expected = Arrays.asList(expected);
    }

    @Test
    public void test() {
        assertEquals(expected, BreadthFirstSearch.getShortestDistance(adj, src, dest));
    }
}