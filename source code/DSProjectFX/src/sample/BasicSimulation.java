package sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BasicSimulation {

    // Declare variables
    ArrayList<Location> list;
    MyDepot mydepot; // Create object of MyDepot class
    Queue<Integer> BFSqueue;
    LinkedList<Location> location = new LinkedList<>();
    ArrayList<Path> Path = new ArrayList<>();
    double adjMatrix[][]; // Create 2D-Array of Adjacent Matrix[row][column]
    double distance, path, cost, totalCost;
    int capacity;
    int ID; // Declare customer ID
    int adjNode;
    boolean result;

    public BasicSimulation(ArrayList<Location> list) { // Constructor
        this.list = list;
        this.mydepot = (MyDepot) list.get(0);
        this.BFSqueue = new LinkedList<>();
        this.distance = 0;
        this.path = Double.MAX_VALUE; // Set the path to a maximum value
        this.cost = 0;
        this.totalCost = 0;
        this.capacity = 0;
        this.ID = 0;
        this.adjNode = 0;
        this.result = false;
        this.calculateDistanceOfAdjacentVertex();
        //this.printDistanceOfAdjacentVertex();
        //this.bfsBreathFirstSearch(); // Call method bfsBreathFirstSearch() to start searching
    }

    public void bfsBreathFirstSearch() {
        // Add customers into BFSqueue
        for (int i = 1; i < list.size(); i++) {
            BFSqueue.add(i);
        }
        // Loop until all customers are visited
        while (allVertexVisited() == false) {
            result = false;
            capacity = 0; // Initialise capacity of vehicle
            location.clear(); // Clear location
            location.add(mydepot); // Start location at mydepot
            if (((MyCustomer) list.get(BFSqueue.peek())).hasVisited) {
                adjNode = BFSqueue.poll();
            }
            else {
                adjNode = BFSqueue.poll();
                location.add(list.get(adjNode));
                capacity += list.get(adjNode).demandSize;
                ((MyCustomer) list.get(adjNode)).hasVisited = true;
                for (int i = 1; i < list.size(); i++) {
                    MyCustomer mycustomer=(MyCustomer) list.get(i);
                    if (((capacity + mycustomer.demandSize) <= mydepot.C) && (mycustomer.hasVisited == false)) {  // mydepot.C is the maximum capacity of mydepot
                        result = true;
                        ID = i;
                        path = adjMatrix[adjNode][i];
                        break;
                    }
                }
                for (int i = 1; i < list.size(); i++) {
                    MyCustomer mycustomer = (MyCustomer) list.get(i);
                    if (((capacity + mycustomer.demandSize) <= mydepot.C) && (mycustomer.hasVisited == false)) { // mydepot.C is the maximum capacity of mydepot
                        if (path > adjMatrix[adjNode][i]) {
                            result = true;
                            ID = i;
                            path = adjMatrix[adjNode][i];
                        }
                    }
                }
                if(result == true){ // If vehicle travel more than 1 customer
                    MyCustomer mycustomer = (MyCustomer) list.get(ID);
                    location.add(mycustomer); // add another customer/s if vehicle travel more than 1 customer
                    capacity += mycustomer.demandSize;
                    mycustomer.hasVisited = true;
                }
                location.add(mydepot); // Return location back to mydepot
                cost = calculateCost(location);
                Path.add(new Path(location, capacity, cost)); // add into an Arraylist
                totalCost += cost; // Sum up the total tourCost
            }
        }
        // Print the output of Basic Simulation: tourCost, vehicle, route and capacity of each vehicle
        //System.out.println("----------------------------------------------------------------\n");
        System.out.println("Basic Simulation");
        System.out.println("Tour");
        showPath(Path);
        System.out.println();
        System.out.println("Tour Cost: " + totalCost);
    }

    public void showPath(ArrayList<Path> path) {
        int j = 0;
        for (int i = 1; i <= path.size(); i++) {
            System.out.println("\nVehicle " + i);
            System.out.println(path.get(j));
            j++;
        }
    }

    // Calculate the cost
    public double calculateCost(LinkedList<Location> location) {
        double cost = 0.0;
        for (int i = 0; i < location.size()-1; i++) {
            cost += adjMatrix[location.get(i).id][location.get(i + 1).id];
        }
        return cost;
    }
/*  computeCost method
          0   1   3     0   2     0   4
          1   3   0     2   0     4   0
         0-1 1-3 3-0   0-2 2-0   0-4 4-0
           0-1-3-0      0-2-0     0-4-0
*/

    public boolean allVertexVisited() {
        for (int i = 1; i < list.size(); i++) {
            if (((MyCustomer) list.get(i)).hasVisited == false) {
                return false;
            }
        }
        return true;
    }

    public void calculateDistanceOfAdjacentVertex() {
        adjMatrix = new double[list.size()][list.size()]; // Create size of 2D-Array Adjacent Matrix[row][column]
        // Calculate distance between two adjacent vertex
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (j == i) { // Self no distance
                    adjMatrix[i][j] = 0; // distance = 0
                }
                // Store the distance between two adjacent vertex
                else { // Use Math.pow to calculate Euclidean distance
                    double x1, x2, y1, y2, xDifference, yDifference;
                    x1 = list.get(i).x;
                    x2 = list.get(j).x;
                    y1 = list.get(i).y;
                    y2 = list.get(j).y;
                    xDifference = x2 - x1; // xDifference -ve is ok because power^2 eventually
                    yDifference = y2 - y1; // yDifference -ve is ok because power^2 eventually
                    distance = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)); // Using formula - Math.sqrt & Math.pow
                    adjMatrix[i][j] = distance;
                }
            }
        }
    }

    // Display 2D-Array of Adjacent Matrix[row][column] storing distance between two adjacent vertex
    public void printDistanceOfAdjacentVertex() {
        System.out.println("Distance between Adjacent Vertex of the Graph");
        System.out.println("");
        System.out.print("             ");
        for(int i = 0; i < adjMatrix.length; i++){
            System.out.print("Vertex " + i);
            if(i<10){
                System.out.print(" ");
            }
            System.out.print("             ");
        }
        System.out.println("");

        for (int i = 0; i < adjMatrix.length; i++) {
            System.out.print("Vertex " + i + " ");
            if(i<10){
                System.out.print(" ");
            }
            for (int j = 0; j < adjMatrix.length; j++) {
                System.out.printf("%-3s", " ");
                if(adjMatrix[i][j]==0){
                    System.out.printf("%.0f", adjMatrix[i][j]);
                    System.out.print("                  ");
                    continue;
                }
                else{
                    System.out.printf("%.14f", adjMatrix[i][j]);
                    if(adjMatrix[i][j]<10){
                        System.out.print("   ");
                    }
                    else if(adjMatrix[i][j]<100){
                        System.out.print("  ");
                    }
                    else{
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
        //System.out.println("");
    }

}