package test;

import org.junit.Test;
import sol.Dijkstra;
import sol.Transport;
import sol.TravelController;
import sol.TravelGraph;
import src.IDijkstra;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly, but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 */
public class DijkstraTest {
    /*
     * Testing plan for Dijkstra:
     *
     * Test for the cheapest route from New York City to buffalo,
     *     since it will involve crossovers instead of just taking the plane
     * Cheapest way to get from Buffalo to NYC
     * Two cities that are unreachable
     * Source and destination being the same city
     *
     * In cities/transport4:
     * Test for paris to barcelona.
     * If using Dijkstra it will require many  crossovers and if using BFS it
     * will be wildly expensive and time-consuming but direct.
     */
    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    private TravelController controller;


    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    /**
     * A sample test that tests Dijkstra's on a simple graph. Checks that the shortest path using Dijkstra's is what we
     * expect.
     */
    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
                dijkstra.getShortestPath(this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    /**
     * we are buff
     * route that requires 3 hops
     */
    @Test
    public void cheapestNYtoBuff() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        int actual = this.controller.cheapestRoute("New York City", "Buffalo").size();
        assertEquals(3, actual);
    }

    /**
     * can't reach it :( so sad
     * (except it's not sad because we return an empty path)
     */
    @Test()
    public void testUnreachableEmptyPath() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        List<Transport> rtFast = this.controller.fastestRoute("Rochester", "Albany");
        List<Transport> rtCheap = this.controller.cheapestRoute("Rochester", "Albany");
        List<Transport> empty = new LinkedList<>();
        assertEquals(rtFast, empty);
        assertEquals(rtCheap, empty);
    }

    /**
     * roses are red
     * violets are blue
     * these cities are the same
     * and this poem doesn't rhyme
     */
    @Test
    public void thoseAreEqual() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        int actual = this.controller.cheapestRoute("Rochester", "Rochester").size();
        assertEquals(0, actual);
    }

    /**
     * Have you ever thought
     * It would be a good idea
     * To write a haiku...
     * <p>
     * for your javadoc? We didn't until just now.
     * This method tests NYC to Buffalo but for fastest instead of cheapest.
     */
    @Test
    public void nycToBuff() {
        this.controller = new TravelController();
        this.controller.load("data/cities3.csv", "data/transport3.csv");
        int actual = this.controller.fastestRoute("New York City", "Buffalo").size();
        assertEquals(1, actual);
    }

    /**
     * "These ones are the crazy edge cases" -Levi Neuwirth, 2023
     * (4 hops)
     * Also... $1 flights that take 1 min to fly hundreds of miles?? sign me up!
     */
    @Test
    public void barcelonaCheap() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        int actual = this.controller.cheapestRoute("Paris", "Barcelona").size();
        assertEquals(4, actual);
    }

    /**
     * zoom zoom (another 4-hop test for fastest because... again... 1 minute!!)
     */
    @Test
    public void barcelonaFast() {
        this.controller = new TravelController();
        this.controller.load("data/cities4.csv", "data/transport4.csv");
        int actual = this.controller.fastestRoute("Paris", "Barcelona").size();
        assertEquals(4, actual);
    }

}
