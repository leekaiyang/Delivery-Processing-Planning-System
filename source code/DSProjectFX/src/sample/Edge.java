package sample;

public class Edge <E>{

    Vertex<E> toVertex;
    Edge<E> nextEdge;

    public Edge(){ // Default constructor
        this.toVertex = null;
        this.nextEdge = null;
    }

    public Edge(Vertex<E> toVertex, Edge<E> nextEdge){ // Constructor
        this.toVertex = toVertex;
        this.nextEdge = nextEdge;
    }
}
