package overlay.util;

import java.util.*;
import java.util.stream.Collectors;

public class BreadthFirstSearch {

    public static List<Integer> getShortestDistance(int[][] adj, int vertices, int src, int dest) {
        List<Integer> predecessors = new ArrayList<>(vertices);
        Queue<Integer> queue = new LinkedList<>();
        List<Boolean> visited = new ArrayList<>(vertices);

        for (int i = 0; i < vertices; i++) {
            visited.add(false);
            predecessors.add(-1);
        }

        visited.set(src, true);
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < vertices; i++) {
                if (adj[u][i] == 1 && !visited.get(i)) {
                    visited.set(i, true);
                    predecessors.set(i, u);
                    queue.add(i);
                    if (i == dest) {
                        queue.clear();
                        break;
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int crawl = dest;
        path.add(crawl);
        while (predecessors.get(crawl) != -1) {
            path.add(predecessors.get(crawl));
            crawl = predecessors.get(crawl);
        }
        Collections.reverse(path);

        return path;
    }
}
