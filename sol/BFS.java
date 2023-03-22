package sol;

import src.IBFS;
import src.IEdge;
import src.IGraph;
import src.IVertex;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Implementation for BFS, implements IBFS interface
 *
 * @param <V> The type of the vertices
 * @param <E> The type of the edges
 */
public class BFS<V extends IVertex<E>, E extends IEdge<V>>
        extends SearchAlgo<V, E> implements IBFS<V, E> {
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        if (start.equals(end)) return new LinkedList();
        LinkedList<V> toCheck = new LinkedList<>();
        HashSet<V> visited = new HashSet<>();
        HashMap<V, E> originMap = new HashMap<>();

        toCheck.addLast(start);
        visited.add(start);

        while (!toCheck.isEmpty()) {
            V checkingVertex = toCheck.removeFirst(); // BFS
            if (end.equals(checkingVertex)) {
                return this.backtrack(end, start, originMap, new LinkedList<>());
            }
            for (E neighborEdge : checkingVertex.getOutgoing()) {
                V neighbor = neighborEdge.getTarget();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    toCheck.addLast(neighbor);
                    originMap.put(neighbor, neighborEdge);
                }
            }
        }
        return new LinkedList();
    }
}
