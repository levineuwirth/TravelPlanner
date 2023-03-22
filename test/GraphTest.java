package test;

import org.junit.Before;
import org.junit.Test;
import sol.City;
import sol.Transport;
import sol.TravelController;
import sol.TravelGraph;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 */
public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private SimpleEdge edgeAB;
    private SimpleEdge edgeBC;
    private SimpleEdge edgeCA;
    private SimpleEdge edgeAC;

    TravelController controller;
    TravelGraph betterGraph;

    /**
     * sets up the travel graph model and controller
     */
    @Before
    public void setup() {
        this.controller = new TravelController();
        this.betterGraph = new TravelGraph(this.controller);
    }

    /**
     * Tests operations involving vertices
     */
    @Test
    public void testVertices() {
        City nyc = new City("New York");
        City albany = new City("Albany");
        this.betterGraph.addVertex(nyc);
        this.betterGraph.addVertex(albany);

        Set<City> expected = new HashSet<>();
        expected.add(nyc);
        expected.add(albany);

        // Tests addVertex
        assertEquals(this.betterGraph.getVertices(), expected);
        // Tests doTheCities with a present city
        assertEquals(this.betterGraph.doTheCities("New York").toString(), "New York");

        //Tests doTheCities with a new city being added
        this.betterGraph.doTheCities("Chicago");
        assertEquals(this.betterGraph.doTheCities("Chicago").toString(), "Chicago");
    }

    /**
     * Tests that getCityObject throws an exception when the city isn't found.
     * The normal functionality of this is tested through doTheCities.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCityObjectException() {
        this.betterGraph.getCityObject("Los Angeles");
    }

    /**
     * Tests operations involving edges
     */
    @Test
    public void testEdges() {
        City nyc = new City("New York");
        City albany = new City("Albany");
        this.betterGraph.addVertex(nyc);
        this.betterGraph.addVertex(albany);
        Transport edgeTest = new Transport(nyc, albany, TransportType.TRAIN, 45, 175);
        this.betterGraph.addEdge(nyc, edgeTest);
        Set<Transport> expected = new HashSet<>();
        expected.add(edgeTest);

        // tests getOutgoingEdges
        assertEquals(this.betterGraph.getOutgoingEdges(nyc), expected);
        // tests initialization of the HashSet when city is added to the HashMap
        assertEquals(this.betterGraph.getOutgoingEdges(albany).isEmpty(), true);
    }

    /**
     * Tests the transport-related methods
     */
    @Test
    public void testTransportMethods() {
        City nyc = new City("New York");
        City albany = new City("Albany");
        Transport transTest = new Transport(nyc, albany, TransportType.TRAIN, 45, 175);
        assertEquals(this.betterGraph.getEdgeSource(transTest), nyc);
        assertEquals(this.betterGraph.getEdgeTarget(transTest), albany);
    }

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     * <p>
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    /**
     * Sample test for the graph. Tests that the number of vertices and the vertices in the graph are what we expect.
     */
    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        // test getVertices to check this method AND insertVertex
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    /**
     * Tests that we can't get an edge for a city that hasn't been added as a vertex.
     */
    @Test
    public void wheresThatCity() {
        City nyc = new City("New York");
        Set<Transport> a = this.betterGraph.getOutgoingEdges(nyc);
        assertNull(a);
    }
}
