package sample;

import java.util.LinkedList;

public class Path {
    LinkedList<Location> location;
    int capacity;
    double cost;

    public Path() { // Default constructor
        int capacity = 0;
        double cost = 0;
    }

    public Path(LinkedList<Location> location, int capacity, double cost) { // Constructor
        this.location = (LinkedList<Location>) location.clone();
        this.capacity = capacity;
        this.cost = cost;
    }

    @Override
    public String toString() {
        String str = "Route: ";
        for (int i = 0; i < location.size() - 1; i++) {
            str += (location.get(i).id + " -> ");
        }
        return str + location.get(location.size() - 1).id
                + "\nCapacity: " + capacity
                + "\nCost: " + cost;
    }
}