package sample;

public class Vertex<E>{

    E vertexInfo;
    Vertex<E> nextVertex;
    Edge<E> firstEdge;
    int CoordinateX;
    int CoordinateY;
    int demandSize;

    public Vertex(){ // Default constructor
        this.vertexInfo = null;
        this.nextVertex = null;
        this.firstEdge = null;
        this.CoordinateX = 0;
        this.CoordinateY = 0;
        this.demandSize = 0;
    }

    public Vertex(E vertexInfo, Vertex<E> nextVertex, int CoordinateX, int CoordinateY, int demandSize){ // Constructor
        this.vertexInfo = vertexInfo;
        this.nextVertex = nextVertex;
        this.firstEdge = null;
        this.CoordinateX = CoordinateX;
        this.CoordinateY = CoordinateY;
        this.demandSize = demandSize;
    }
}
