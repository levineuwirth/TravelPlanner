package sol;

import src.IEdge;
import src.ITransport;
import src.IVertex;
import src.TransportType;

/**
 * A Transport class representing the edge of a travel graph
 */
public class Transport implements IEdge<City>, ITransport {
    City source;
    City destination;
    TransportType type;
    double price;
    double minutes;

    /**
     * Constructs a Transport with the specified arguments
     *
     * @param source      Source city (for this edge)
     * @param destination Destination city (for this edge)
     * @param type        Type/method of transport
     * @param price       The price
     * @param minutes     The time in minutes
     */
    public Transport(City source, City destination, TransportType type,
                     double price, double minutes) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.minutes = minutes;
    }

    @Override
    public City getSource() {
        return this.source;
    }

    @Override
    public City getTarget() {
        return this.destination;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public double getMinutes() {
        return this.minutes;
    }

    @Override
    public String getType() {
        return this.type.toString();
    }

    @Override
    public String toString() {
        return this.getSource().toString() + " -> " +
                this.getTarget().toString() +
                ", Type: " + this.getType() +
                ", Cost: $" + this.getPrice() +
                ", Duration: " + this.getMinutes() + " minutes";
    }
}
