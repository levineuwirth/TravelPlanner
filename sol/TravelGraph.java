package sol;

import src.IGraph;

import java.util.Set;
import java.util.HashMap;

/**
 * Implementation for TravelGraph
 */
public class TravelGraph implements IGraph<City, Transport> {
    private HashMap<City, Set<Transport>> routesByCity;
    private TravelController theController;
    private HashMap<String, City> cities;

    /**
     * Constructs a new TravelGraph
     *
     * @param controller the TravelController that is controlling this graph
     */
    public TravelGraph(TravelController controller) {
        this.theController = controller;
        this.routesByCity = new HashMap<>();
        this.cities = new HashMap<>();
    }

    @Override
    public void addVertex(City vertex) {
        this.routesByCity.put(vertex, vertex.getOutgoing());
    }

    @Override
    public void addEdge(City origin, Transport edge) {
        this.routesByCity.get(origin).add(edge);
    }

    @Override
    public Set<City> getVertices() {
        return this.routesByCity.keySet();
    }

    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return this.routesByCity.get(fromVertex);
    }

    /**
     * "Let's make a method to do the cities" -Levi Neuwirth, 2023
     * Returns a city by name if it was initialized.
     * Otherwise, adds it to the cities hashmap.
     *
     * @param cityName name of the city
     * @return city with things done
     */
    public City doTheCities(String cityName) {
        try {
            return this.getCityObject(cityName);
        } catch (IllegalArgumentException e) {
            City toInsert = new City(cityName);
            this.cities.put(cityName, toInsert);
            return toInsert;
        }
    }

    /**
     * Attempts to get a city by name, but doesn't update the hashmap if it
     * isn't found.
     *
     * @param cityName the name of the City for which we are searching
     * @return the city object if it is found
     * @throws IllegalArgumentException if that city doesn't exist
     */
    public City getCityObject(String cityName) throws IllegalArgumentException {
        if (this.cities.containsKey(cityName)) {
            return this.cities.get(cityName);
        } else throw new IllegalArgumentException(cityName + " not found");
    }
}
