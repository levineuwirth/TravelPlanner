package sol;

import src.IVertex;
import src.IEdge;
import src.IDijkstra;
import src.IGraph;

import java.util.Comparator;
import java.util.function.Function;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implementation for Dijkstra's algorithm
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
public class Dijkstra<V extends IVertex<E>, E extends IEdge<V>>
        extends SearchAlgo<V, E> implements IDijkstra<V, E> {

    /**
     * An implementation of Dijkstra's algorithm.
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight  the weight of an edge
     * @return the shortest path or an exception if there is no such path.
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        if(source.equals(destination)) return new LinkedList();
        Set<V> cities = graph.getVertices();
        HashMap<V, Double> routeDistTo = new HashMap<>();

        Comparator<V> lowerCost = Comparator.comparingDouble(routeDistTo::get);
        PriorityQueue<V> cityQueue = new PriorityQueue<>(lowerCost);

        for (V city : cities) {
            routeDistTo.put(city, Double.MAX_VALUE);
            cityQueue.add(city);
        }
        routeDistTo.put(source, 0.0);

        HashMap<V, E> originMap = new HashMap<>();

        while (!cityQueue.isEmpty()) {
            V checkingVertex = cityQueue.poll();
            for (E neighborEdge : checkingVertex.getOutgoing()) {
                V neighbor = neighborEdge.getTarget();

                double newDistToNeighbor = routeDistTo.get(checkingVertex)
                        + edgeWeight.apply(neighborEdge);
                if (newDistToNeighbor < routeDistTo.get(neighbor)) {
                    routeDistTo.put(neighbor, newDistToNeighbor);
                    originMap.put(neighbor, neighborEdge);

                    // decrease the neighbor edge's value in the PQ
                    cityQueue.remove(neighbor);
                    cityQueue.add(neighbor);
                }
            }
        }

        if (originMap.containsKey(destination)) {
            return this.backtrack(
                    destination, source, originMap, new LinkedList<>());
        } else {
            if(source.equals(destination)) return new LinkedList();
        }
        return new LinkedList();
    }

}
