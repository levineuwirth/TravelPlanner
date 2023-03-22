package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sol.TravelController;
import sol.TravelGraph;
import sol.Transport;
import sol.BFS;
import sol.City;
import src.IVertex;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly, but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;
    private TravelGraph betterGraph;
    private TravelController controller;
    private BFS<City, Transport> bfs;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    /**
     * A sample test that tests BFS on a simple graph. Checks that running BFS gives us the path we expect.
     */
    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    /**
     * sets up the travel graph model and controller
     */
    @Before
    public void setup() {
        this.controller = new TravelController();
        this.betterGraph = new TravelGraph(this.controller);
        this.bfs = new BFS<>(); // comedy gold
    }

    /**
     * Test BFS route finding
     */
    @Test
    public void testRudimentaryFindingRoutes() {
        City nyc = new City("New York");
        City albany = new City("Albany");
        City np = new City("New Paltz");
        Transport edge1 = new Transport(nyc, np, TransportType.TRAIN, 45, 175);
        Transport edge2 = new Transport(nyc, albany, TransportType.TRAIN, 45, 175);
        Transport edge3 = new Transport(albany, np, TransportType.TRAIN, 45, 175);
        this.betterGraph.addVertex(nyc);
        this.betterGraph.addVertex(albany);
        this.betterGraph.addVertex(np);
        this.betterGraph.addEdge(nyc, edge1);
        this.betterGraph.addEdge(nyc, edge2);
        this.betterGraph.addEdge(albany, edge3);
        LinkedList<Transport> compare = new LinkedList<>();
        compare.addFirst(edge1);
        assertEquals(compare, this.bfs.getPath(this.betterGraph, nyc, np));
    }

    /**
     * Tests using the load() method.
     */
    @Test
    public void testBFSFromLoad() {
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        assertEquals(3, this.controller.mostDirectRoute(
                "Buffalo", "New Paltz").size());
        assertEquals(2, this.controller.mostDirectRoute(
                "Buffalo", "Rochester").size());
        assertEquals(1, this.controller.mostDirectRoute(
                "New York City", "Albany").size());
    }

    /**
     * Makes sure the empty path is returned when two cities
     * cannot be reached
     */
    @Test()
    public void testUnreachableEmptyPath() {
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        List<Transport> rt = this.controller.mostDirectRoute("Rochester", "Albany");
        assertEquals(rt, new LinkedList<>());
    }

    /**
     * Those are the same city! We don't care about travelling from college hill to fox point :)
     */
    @Test
    public void thoseAreEqual() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        int actual = this.controller.mostDirectRoute("Rochester", "Rochester").size();
        assertEquals(0, actual);
    }

    /**
     * Tests that BFS can recognize a single route.
     */
    @Test
    public void nycToBuffalo() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        int actual = this.controller.mostDirectRoute("New York City", "Buffalo").size();
        assertEquals(1, actual);
    }

    /**
     * "These ones are the crazy edge cases" -Levi Neuwirth, 2023
     * makes sure that BFS really retains its integrity even when making a really bad choice.
     * (Rejects way cheaper and faster routes for the least individual ways)
     */
    @Test
    public void parisToBarcelona() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        int actual = this.controller.mostDirectRoute("Paris", "Barcelona").size();
        assertEquals(1, actual);
    }

    /**
     * Tests if BFS can find the path with the least hops.
     */
    @Test
    public void pathMoreThanOne() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        int actual = this.controller.mostDirectRoute("London", "The Hague").size();
        assertEquals(2, actual);
    }

    /**
     * edge case: source city doesn't exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void mostDirectCitySourceDoesntExist() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        this.controller.mostDirectRoute("The FitnessGram Pacer Test", "The Hague");
    }

    /**
     * edge case: destination city doesn't exist
     */
    @Test(expected = IllegalArgumentException.class)
    public void mostDirectCityDestDoesntExist() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        this.controller.mostDirectRoute("London", "is a multi-stage aerobic capacity test");
    }
}
