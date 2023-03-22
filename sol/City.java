package sol;

import src.IEdge;
import src.IVertex;

import java.util.HashSet;
import java.util.Set;

/**
 * A City class representing the vertex of a travel graph
 */
public class City implements IVertex<Transport> {
    private String name;
    private HashSet<Transport> routes;

    /**
     * Constructor for a City
     *
     * @param name The name of the city
     */
    public City(String name) {
        this.name = name;
        this.routes = new HashSet<>();
    }

    @Override
    public Set<Transport> getOutgoing() {
        return this.routes;
    }

    @Override
    public void addOut(Transport outEdge) {
        this.routes.add(outEdge);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
