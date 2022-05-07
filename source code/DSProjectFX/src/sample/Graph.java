package sample;

import java.util.ArrayList;

public class Graph <E extends Comparable<E>>{

    Vertex<E> head;
    int size;

    public Graph(){ // Default constructor
        this.head = null;
        this.size = 0;
    }

    public void clear(){
        this.head = null;
    }

    public int getSize(){
        return this.size;
    }

    public boolean hasVertex(E vertex){
        if (head == null){
            return false;
        }
        Vertex<E> temp = head;
        while(temp != null){
            if(temp.vertexInfo.compareTo(vertex) == 0){
                return true;
            }
            temp = temp.nextVertex;
        }
        return false;
    }

    public boolean addVertex(E vertex, int CoordinateX, int CoordinateY, int demandSize){
        if (hasVertex(vertex) == false){
            Vertex<E> temp = head;
            Vertex<E> newVertex = new Vertex<>(vertex, null, CoordinateX, CoordinateY, demandSize) ;
            if(head == null){
                head = newVertex;
            }
            else{
                Vertex<E> previous = head;
                while(temp != null){
                    previous = temp;
                    temp = temp.nextVertex;
                }
                previous.nextVertex = newVertex;
            }
            size++;
            return true;
        }
        else{
            return false;
        }
    }

    public E getVertex(int position){
        if ( (position > size-1) || (position < 0) ){
            return null;
        }
        Vertex<E> temp = head;
        for (int i = 0; i < position; i++){
            temp = temp.nextVertex;
        }
        return temp.vertexInfo;
    }

    public ArrayList<E> AdjacentVertex(E vertex){
        if (!hasVertex(vertex)){
            return null;
        }
        ArrayList<E> list = new ArrayList<E>();
        Vertex<E> temp = head;
        while (temp != null){
            if (temp.vertexInfo.compareTo(vertex) == 0){
                // Reached vertex, look for destination now
                Edge<E> currentEdge = temp.firstEdge;
                while (currentEdge != null){
                    list.add(currentEdge.toVertex.vertexInfo);
                    currentEdge = currentEdge.nextEdge;
                }
            }
            temp = temp.nextVertex;
        }
        return list;
    }

    public ArrayList<E> getAllVertexObjects(){
        ArrayList<E> list = new ArrayList<>();
        Vertex<E> temp = head;
        while(temp != null) {
            list.add(temp.vertexInfo);
            temp = temp.nextVertex;
        }
        return list;
    }

    public ArrayList<Vertex<E>> getAllVertices(){
        ArrayList<Vertex<E>> list = new ArrayList<>();
        Vertex<E> temp = head;
        while (temp != null) {
            list.add(temp);
            temp = temp.nextVertex;
        }
        return list;
    }

    public int getIndex(E vertex){
        Vertex<E> temp = head;
        int pos = 0;
        while(temp != null) {
            if (temp.vertexInfo.compareTo(vertex) == 0){
                return pos;
            }
            temp = temp.nextVertex;
            pos += 1;
        }
        return -1;
    }

    public boolean hasEdge(E source, E destination){
        if (head==null){
            return false;
        }
        if ( !hasVertex(source) || !hasVertex(destination )){
            return false;
        }
        Vertex<E> sourceVertex = head;
        while( sourceVertex != null ){
            if (sourceVertex.vertexInfo.compareTo(source) == 0){
                // Reached source vertex, look for destination now
                Edge<E> currentEdge = sourceVertex.firstEdge;
                while(currentEdge != null){
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0){ // destination vertex found
                        return true;
                    }
                    currentEdge = currentEdge.nextEdge;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return false;
    }

    public boolean addEdge(E source, E destination){
        if (head == null){
            return false;
        }
        if ( !hasVertex(source) || !hasVertex(destination) ){
            return false;
        }
        Vertex<E> sourceVertex = head;
        while(sourceVertex != null){
            if (sourceVertex.vertexInfo.compareTo(source) == 0){
                // Reached source vertex, look for destination now
                Vertex<E> destinationVertex = head;
                while ( destinationVertex != null ){
                    if (destinationVertex.vertexInfo.compareTo(destination) == 0){
                        // Reached destination vertex, add edge here
                        Edge<E> currentEdge = sourceVertex.firstEdge;
                        Edge<E> newEdge = new Edge<>(destinationVertex, currentEdge);
                        sourceVertex.firstEdge = newEdge;
                        return true;
                    }
                    destinationVertex = destinationVertex.nextVertex;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return false;
    }

    public int demandSize(E vertex){
        if (hasVertex(vertex) == true){
            Vertex<E> temp = head;
            while(temp != null){
                if (temp.vertexInfo.compareTo(vertex)==0)
                    return temp.demandSize;
                temp =temp.nextVertex;
            }
        }
        return -1;
    }

    public int coordinateX(E vertex){
        if (hasVertex(vertex) == true){
            Vertex<E> temp = head;
            while(temp != null){
                if (temp.vertexInfo.compareTo(vertex) ==0)
                    return temp.CoordinateX;
                temp = temp.nextVertex;
            }
        }
        return -1; // Return -1 if not found
    }

    public int coordinateY(E vertex){
        if (hasVertex(vertex) == true){
            Vertex<E> temp = head;
            while(temp != null){
                if (temp.vertexInfo.compareTo(vertex) ==0)
                    return temp.CoordinateY;
                temp = temp.nextVertex;
            }
        }
        return -1; // Return -1 if not found
    }

    public double calculateDistanceOfAdjVertex(E vertex1, E vertex2){ // Use Math.pow to calculate Euclidean distance
        double x1, x2, y1, y2, xDifference, yDifference, distance;
        x1 = coordinateX(vertex1);
        x2 = coordinateX(vertex2);
        y1 = coordinateY(vertex1);
        y2 = coordinateY(vertex2);
        xDifference = x2 - x1; // xDifference -ve is ok because power^2 eventually
        yDifference = y2 - y1; // yDifference -ve is ok because power^2 eventually
        distance = Math.sqrt( Math.pow(xDifference, 2) + Math.pow(yDifference, 2) ); // Using formula - Math.sqrt & Math.pow
        return distance;
    }

    public double tourCost(ArrayList<ArrayList<E>> tour){
        double cost = 0;
        for (int i = 0; i < tour.size(); i++){
            cost += routeCost(tour.get(i));
        }
        return cost;
    }

    public double routeCost(ArrayList<E> route){
        double cost = 0;
        int j = 1;
        for (int i = 0; i < route.size()-1; i++){
            cost += calculateDistanceOfAdjVertex(route.get(i), route.get(j));
            j++;
        }
        return cost;
    }

    public double totalCost(ArrayList<ArrayList<E>> pathSearch){
        int n = 1;
        double cost = 0;
        System.out.println("Tour");
        for(int k = 0; k < pathSearch.size(); k++){
            System.out.println("\nVehicle " + (n));
            cost += cost(pathSearch.get(k));
            n++;
        }
        System.out.print("\nTour Cost: ");
        return cost;
    }

    private double cost(ArrayList<E> pathSearch){
        int capacity=0, n=0;
        double cost=0;
        System.out.print("Route: ");
        for(int i=0; i<pathSearch.size(); i++){
            if(i == pathSearch.size()-1)
                System.out.print(pathSearch.get(i));
            else{
                System.out.print(pathSearch.get(i) + " -> ");
            }
        }
        System.out.println("");
        while(n < pathSearch.size()-1){ //calculates the pathCost
            cost += calculateDistanceOfAdjVertex(pathSearch.get(n), pathSearch.get(n+1));
            n++;
        }
        for(int j=0; j<pathSearch.size(); j++){ //calculates the vehicleLoad
            capacity += demandSize(pathSearch.get(j));
        }
        System.out.println("Capacity: " + capacity);
        System.out.println("Cost: " + cost);
        return cost;
    }

}
