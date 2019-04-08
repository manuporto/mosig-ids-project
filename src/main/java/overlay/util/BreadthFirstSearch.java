package overlay.util;

import java.util.*;

/**
 * Class used to calculate the shortest distance between two nodes
 * of the network and find the next hops using a Breadth first search
 * alike algorithm.
 */
public class BreadthFirstSearch {

    /**
     * Method to calculate the shortest distance between two nodes.
     * @param adj is a list of adjacent nodes.
     * @param src is the ID of the source node.
     * @param dest is the ID of the destination node.
     * @return a list containing the shortest path to destination node.
     */
    static List<Integer> getShortestDistance(List<List<Integer>> adj, int src, int dest) {
        List<Integer> predecessors = new ArrayList<>(adj.size());
        Queue<Integer> queue = new LinkedList<>();
        List<Boolean> visited = new ArrayList<>(adj.size());

        for (int i = 0; i < adj.size(); i++) {
            visited.add(false);
            predecessors.add(-1);
        }

        visited.set(src, true);
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.size(); i++) {
                if (adj.get(u).get(i) == 1 && !visited.get(i)) {
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

    /**
     * Method that will calculate the next hops.
     * @param src is the ID of the source node.
     * @param adj is a list of adjacent nodes.
     * @return a map of next hops to follow.
     */
    public static Map<Integer, Integer> calculateNextHops(int src, List<List<Integer>> adj) {
        Map<Integer, Integer> hops = new HashMap<>();
        int nextHop;
        for (int dest = 0; dest < adj.size(); dest++) {
            if ( dest != src) {
                nextHop = BreadthFirstSearch.getShortestDistance(adj, src, dest).get(1);
                hops.put(dest, nextHop);
            }
        }

        return hops;
    }
}
