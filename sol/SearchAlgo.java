package sol;

import src.IEdge;
import src.IVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation for a backtrackable search algorithm
 *
 * @param <V> The type of the vertices
 * @param <E> The type of the edges
 */
public abstract class SearchAlgo<V extends IVertex<E>, E extends IEdge<V>> {

    /**
     * recursively backtracks from the destination city to the origin city to
     * calculate the path we used to get from origin to destination
     *
     * @param currentCity the vertex we're currently back to
     * @param startCity   the starting vertex - the one we want to backtrack to
     * @param originMap   maps cities to the edge that led to them w/BFS routing
     * @param pathSoFar   cities already backtracked - used for recursion
     * @return the calculated path of edges from the start to the end city
     */
    protected List<E> backtrack(V currentCity, V startCity, Map<V, E> originMap,
                              LinkedList<E> pathSoFar) {
        if (currentCity.equals(startCity)) {
            return pathSoFar; // base case
        }

        E priorToCurrent = originMap.get(currentCity);
        V prior = priorToCurrent.getSource();

        pathSoFar.addFirst(priorToCurrent);

        return this.backtrack(prior, startCity, originMap, pathSoFar);
    }
}
