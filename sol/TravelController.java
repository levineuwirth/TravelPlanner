package sol;

import src.ITravelController;
import src.TransportType;
import src.TravelCSVParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation for TravelController
 */
public class TravelController implements ITravelController<City, Transport> {
    private TravelGraph graph;


    /**
     * Constructor for TravelController
     */
    public TravelController() {
        this.graph = new TravelGraph(this);
    }

    @Override
    public String load(String citiesFile, String transportFile) {
        TravelCSVParser parser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            // make sure this is the same City object to keep stuff synchronized
            this.graph.addVertex(this.graph.doTheCities(map.get("name")));
            return null;
        };

        Function<Map<String, String>, Void> addTransport = map -> {
            City origin = this.graph.doTheCities(map.get("origin"));
            City destination = this.graph.doTheCities(map.get("destination"));
            TransportType type = TransportType.fromString(map.get("type"));
            double price = Double.parseDouble(map.get("price"));
            double duration = Double.parseDouble(map.get("duration"));

            Transport transport = new Transport(
                    origin, destination, type, price, duration);

            this.graph.addEdge(origin, transport);
            return null;
        };

        try {
            // create City vertices using string for CSV and function
            parser.parseLocations(citiesFile, addVertex);

            // create Transport edges using CSV and function
            parser.parseTransportation(transportFile, addTransport);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }

        return "Successfully loaded cities and transportation files.";
    }

    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Dijkstra d = new Dijkstra();
        City from = this.graph.getCityObject(source);
        City to = this.graph.getCityObject(destination);
        Function<Transport, Double> fastest = (t) -> {
            return t.getMinutes();
        };
        return d.getShortestPath(this.graph, from, to, fastest);
    }

    @Override
    public List<Transport> cheapestRoute(String source, String destination) {

        Dijkstra d = new Dijkstra();
        City from = this.graph.getCityObject(source);
        City to = this.graph.getCityObject(destination);
        Function<Transport, Double> cheapest = (t) -> {
            return t.getPrice();
        };
        return d.getShortestPath(this.graph, from, to, cheapest);
    }

    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        City from = this.graph.getCityObject(source);
        City to = this.graph.getCityObject(destination);

        BFS bfs = new BFS();
        return bfs.getPath(this.graph, from, to);
    }

    /**
     * Returns the graph stored by the controller
     *
     * NOTE: You __should not__ be using this in your implementation,
     * this is purely meant to be used for the visualizer
     *
     * @return The TravelGraph currently stored in the TravelController
     */
    public TravelGraph getGraph() {
        return this.graph;
    }
}
