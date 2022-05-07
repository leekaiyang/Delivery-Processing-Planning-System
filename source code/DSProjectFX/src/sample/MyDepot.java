package sample;

public class MyDepot extends Location{
    int N, C;

    public MyDepot() { // Default constructor
        this.N = 0;
        this.C = 0;
    }

    public MyDepot(int N, int maxCapacity, int x, int y) { // Constructor
        super(x, y, maxCapacity);
        this.N = N;
        this.C = super.demandSize;
    }

    // Getter & Setter
    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getC() {
        return C;
    }

    public void setC(int C) {
        this.C = C;
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

    @Override
    public String toString() {
        return "Number of Depot & Customers, N: " + N
                + "\nMaximum capacity, C: " + C
                + "\n\n~~~~~~~~~~~~~~~~~~ Depot Details ~~~~~~~~~~~~~~~~~~"
                + "\nDepot " + id
                + " (x = " + x
                + ", y = " + y
                + ", Maximum capacity = " + C +
                ") ID = "+ id
                + "\n";
    }

}