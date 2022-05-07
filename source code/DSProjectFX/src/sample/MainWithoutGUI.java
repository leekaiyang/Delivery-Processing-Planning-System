package sample;

import java.io.File;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MainWithoutGUI {

    static ArrayList<Location> location = new ArrayList<Location>();
    static int N, C;
    static int[] vertex = null;


    public static void main(String[] args) throws IOException {
        File file = new File("input.txt");

        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            int N = sc.nextInt();
            int C = sc.nextInt();
            sc.nextLine();
            int X = sc.nextInt();
            int Y = sc.nextInt();
            int depotCap = sc.nextInt();
            location.add(new MyDepot(N, C, X, Y)); //instantiate MyDepot
            sc.nextLine();
            while (sc.hasNextLine()) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                int cap = sc.nextInt();
                location.add(new MyCustomer(x, y, cap)); //instantiate MyCustomer
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        System.out.println(location.get(0));
        System.out.println("~~~~~~~~~~~~~~~~ Customers Details ~~~~~~~~~~~~~~~~");
        for (int i = 1; i < location.size(); i++) {
            System.out.println(location.get(i));
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");


        BasicSimulation basicSimulation = new BasicSimulation(location);
        basicSimulation.printDistanceOfAdjacentVertex();
        System.out.println("----------------------------------------------------\n");

        basicSimulation.bfsBreathFirstSearch();
        System.out.println("----------------------------------------------------\n");


        GreedySearch greedySearch = new GreedySearch();
        greedySearch.solve(file);
        System.out.println("----------------------------------------------------\n");

        aStar a = new aStar();
        a.route(file);
        System.out.println("----------------------------------------------------\n");


        Graph<Integer> graph = new Graph<Integer>();
        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            N = sc.nextInt();
            C = sc.nextInt();
            int n = 0;
            vertex = new int[N];
            while(sc.hasNext()) {
                vertex[n] = n;
                int x = sc.nextInt();
                int y = sc.nextInt();
                int cap = sc.nextInt();
                graph.addVertex(vertex[n], x, y, cap);
                n++;
            }
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        for (int j=0; j<graph.getSize(); j++) {
            for (int k=0; k<graph.getSize(); k++) {
                if (j == k){
                    continue;
                }
                else{
                    graph.addEdge(vertex[j], vertex[k]);
                }
            }
        }
        System.out.println("MCTS Simulation");
        Scanner in = new Scanner(System.in);
        System.out.print("Enter processing time for MCTS: ");
        int time = in.nextInt();
        MCTSSimulation<Integer> mctsSimulation = new MCTSSimulation(graph, C, time);
        System.out.println(graph.totalCost(mctsSimulation.pathSearch));
        System.out.println("----------------------------------------------------\n");
    }

}