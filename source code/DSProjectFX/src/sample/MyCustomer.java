package sample;

public class MyCustomer extends Location {
    boolean hasVisited;

    public MyCustomer() { // Default constructor
        hasVisited = false;
    }

    public MyCustomer(int x, int y, int capacity) { // Constructor
        super(x,y,capacity);
    }

    // Getter & Setter
    public void setWasVisited(boolean wasVisited) {
        this.hasVisited = wasVisited;
    }

    public int getDemandSize() {
        return demandSize;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Customer " + id
                + " (x = " + x
                + ", y = " + y
                + ", capacity = " + demandSize +
                ") ID = "+ id;
    }
}