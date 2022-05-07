package sample;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class MCTSSimulation<E extends Comparable<E>> {
    // Declare variables
    Graph<E> graph;
    int maxCapacity;
    E depot;
    ArrayList<E> unvisitedVertex;
    int N;
    int lvl;
    int iteration;
    int ALPHA;
    double [][][] policy;
    double [][] globalPolicy;
    double z;
    ArrayList<ArrayList<E>> pathSearch;
    ArrayList<E> checked = new ArrayList<E>();
    int time;

    public MCTSSimulation(Graph<E> graph, int maxCapacity, int time) { // Constructor
        this.graph = graph;
        this.maxCapacity = maxCapacity;
        this.depot = graph.getVertex(0);
        this.unvisitedVertex = graph.AdjacentVertex(depot);
        this.N = graph.getSize();
        this.lvl = 3;
        this.iteration = 100;
        this.ALPHA = 1;
        this.policy = new double[lvl][N][N];
        this.globalPolicy = new double[N][N];
        this.time = time;
        this.pathSearch = mctsSearch(lvl, iteration); // Call method search to start searching
    }

    public ArrayList<ArrayList<E>> mctsSearch(int lvl, int iteration){
        Instant start = Instant.now(); // Start counting
        ArrayList<ArrayList<E>> bestTour = new ArrayList<ArrayList<E>>(); // Initialize best_tour
        if(lvl==0) {
            return mctsRollout();
        }
        else{
            policy[lvl-1] = globalPolicy;
            for(int n = 0; n < iteration; n++) {
                ArrayList<ArrayList<E>> newTour = mctsSearch(lvl-1, iteration); // For iteration from 0 to iteration
                if(n==0) {
                    bestTour = newTour;
                    mctsAdapt(bestTour, lvl);
                }
                else if( graph.tourCost(newTour) < graph.tourCost(bestTour) ) {
                    bestTour = newTour;
                    mctsAdapt(bestTour, lvl);
                }
                // If the searching time is far too long then directly return the best tour we can search of after limited time
                if(Duration.between(start, Instant.now()).getSeconds()>10) { // If processing_time exceed time limit
                    return bestTour;
                }
            }
            globalPolicy = policy[lvl-1];
        }
        return bestTour;
    }

    public void mctsAdapt(ArrayList<ArrayList<E>>aTour, int lvl){
        ArrayList<E> unvisited = new ArrayList<E>();
        unvisited.addAll(unvisitedVertex);
        for(ArrayList<E>route : aTour) { // For every route in a_tour
            for(E stop : route) { // For every stop in route
                policy[lvl-1][(Integer)stop][(Integer)route.get(route.indexOf(stop)+1)] += ALPHA;
                z =0.0;
                for( E move : graph.AdjacentVertex(stop) ) { // For every possible move that can be made by stop
                    if(unvisited.contains(move)){ // If the move is not visited yet
                        z += Math.exp(globalPolicy[(Integer)stop][(Integer)move]);
                    }
                }
                for( E move: graph.AdjacentVertex(stop) ) { // For every possible move that can be made by stop
                    if(unvisited.contains(move)){ // If the move is not visited yet
                        policy[lvl-1][(Integer)stop][(Integer)move] -= ALPHA*( Math.exp(globalPolicy[(Integer)stop][(Integer)move])/z );
                    }
                }
                unvisited.remove(stop); // Set stop as visited
            }
        }
    }

    public ArrayList<ArrayList<E>> mctsRollout(){
        int capacity = 0;
        ArrayList<E> route = new ArrayList<E>();
        route.add(depot);
        ArrayList<ArrayList<E>> newTour = new ArrayList<ArrayList<E>>();
        newTour.add(route); // Initialize new_tour with first route with first stop at 0
        ArrayList<E> unvisited = new ArrayList<E>();
        unvisited.addAll(unvisitedVertex);
        // Loop until all customers visited
        while (unvisited.isEmpty() == false) {
            E currentStop = route.get(route.size() - 1); // currentStop = new_tour last route last stop
            ArrayList<E> possibleSuccessors = graph.AdjacentVertex(currentStop);
            // Find every possible successors that is not yet checked for the currentStop
            for(int n = 0; n < possibleSuccessors.size(); n++) {
                if ( (unvisited.contains(possibleSuccessors.get(n))==false) || (checked.contains(possibleSuccessors.get(n))==true) ) {
                    possibleSuccessors.remove(possibleSuccessors.get(n));
                    n--;
                }
            }
            if(possibleSuccessors.isEmpty()==true) { // If no successors is available
                route.add(depot); // route is completed and should return to depot
                if (unvisited.isEmpty()==true) { // All customers are visited
                    break; // break while loop
                }
                capacity = 0;
                checked.clear();
                route = new ArrayList<>();
                route.add(depot);
                newTour.add(route); // add route into new_tour
                continue;
            }
            E nextStop = mctsSelectNextMove(currentStop, possibleSuccessors);
            // if add nextStop to currentRoute does not violate any rules
            if( unvisited.contains(nextStop)==true && ((capacity+graph.demandSize(nextStop))<=maxCapacity)==true ) {
                route.add(nextStop); // add nextStop to currentRoute
                unvisited.remove(nextStop); // set nextStop as visited
                capacity += graph.demandSize(nextStop);
            }
            else{
                checked.add(nextStop); // set nextStop as checked
            }
            if(unvisited.isEmpty()==true){
                route.add(depot);
            }
        }
        return newTour;
    }

    public E mctsSelectNextMove(E currentStop, ArrayList<E>possibleSuccessors){
        double sum = 0;
        double[] probability = new double[possibleSuccessors.size()]; // Initialize 1d probability array that have same size with possible_successors
        for(int i = 0; i < possibleSuccessors.size(); i++) { // for i=0 to size of possible_successors
            probability[i] = Math.exp(globalPolicy[(Integer) currentStop] [(Integer) possibleSuccessors.get(i)]);
            sum += probability[i];
        }
        double mrand = new Random().nextDouble()*sum;
        int n = 0;
        sum = probability[0];
        while(sum < mrand) {
            sum += probability[++n];
        }
        return possibleSuccessors.get(n);
    }

}

