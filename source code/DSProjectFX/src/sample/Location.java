package sample;

public abstract class Location { // Parent class of MyDepot & MyCustomer
    int x, y, demandSize, id;
    static int num;

    public Location() { // Default constructor
        this.x = 0;
        this.y = 0;
        this.demandSize = 0;
        this.id = 0;
        this.num = 0;
    }

    public Location(int x, int y, int demandSize) { // Constructor
        this.x = x;
        this.y = y;
        this.demandSize = demandSize;
        this.id = num;
        this.num++;
    }

    // Getter & Setter
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

    public int getDemandSize() {
        return demandSize;
    }

    public void setDemandSize(int demandSize) {
        this.demandSize = demandSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

