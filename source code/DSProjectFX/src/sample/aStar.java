package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class aStar {
    int N, C;
    int currCapc, index;
    int IDcount = 0, count = 0;
//    int locationID = 0, lastLocationId, nextLocationId;
    double tourCost = 0;
    int vehId = 1;

    ArrayList<Integer> x = new ArrayList(); //list to store x coordinates
    ArrayList<Integer> y = new ArrayList(); //list to store y coordinates
    ArrayList<Double> distance = new ArrayList(); //store the distance between 2 nodes
    ArrayList<Double> cost = new ArrayList(); //store the cost
    ArrayList<Integer> customerDemand = new ArrayList();
    ArrayList<Integer> ID = new ArrayList();
//    ArrayList<Integer> route = new ArrayList();

    public double calcDist(int i, int j) {
        double distance;
        distance = Math.sqrt(Math.pow((x.get(i) - x.get(j)), 2) + Math.pow((y.get(i) - y.get(j)), 2));
        return distance;
    }

    public double calcH(int curr, int end) {
        double H;
        H = Math.sqrt(Math.pow(x.get(curr) - x.get(end), 2) + Math.pow((y.get(curr) - y.get(end)), 2));
        return H;
    }

    public void readFromTxt(File filename) throws IOException {

        Scanner in = new Scanner(new FileInputStream(filename));

        String str = in.nextLine();
        String[] temp = str.split(" ");

        N = Integer.parseInt(temp[0]);
        C = Integer.parseInt(temp[1]);

        while (in.hasNextLine()) {
            str = in.nextLine();
            temp = str.split(" ");

            x.add(Integer.parseInt(temp[0]));
            y.add(Integer.parseInt(temp[1]));
            customerDemand.add(Integer.parseInt(temp[2]));
            ID.add(IDcount);
            ++IDcount;
        }
        in.close();
    }

    public int lowCost(int parent, int outdeg, int currCapc, ArrayList list) {
//        list.remove(parent);
        double temp;
        double min = 2147483647;
        int index = 0;

        for (int i = 0; i < list.size(); i++) {
            temp = calcDist(parent, i) + calcH(i, 0);
            int acqDemand = (int) list.get(i);
            if (min > temp && i != outdeg && i != parent && currCapc >= acqDemand) {
                min = temp;
                index = i;
            }
        }
        return index;
    }

    public void route(File filename) throws IOException {
        readFromTxt(filename);
        System.out.println("A* Simulation");
        System.out.println("Tour\n");


        currCapc = C;

        int parent = 0;
        int outdeg = 0;
        ArrayList visited = new ArrayList();
        int vehCapc = 0;

        while (currCapc != 0 && currCapc > 0) {
            index = lowCost(parent, outdeg, currCapc, customerDemand);
            double currDist = calcDist(parent, index);
            distance.add(currDist);
            visited.add(index);

            if (currCapc > customerDemand.get(index)) {
                currCapc = currCapc - customerDemand.get(index);
                System.out.println("Vehicle " + vehId);
                System.out.println("Route : " + ID.get(parent) + " -> " + ID.get(index));
                System.out.println("Cost : " + calcDist(parent, index));
                cost.add(calcDist(parent, index));
                vehCapc = vehCapc + customerDemand.get(index);
                System.out.println("Capacity of vehicle " + vehId + " : " + vehCapc + "\n");
                outdeg = parent;
                parent = index;
                index = lowCost(parent, outdeg, currCapc, customerDemand);
                distance.add(calcDist(parent, index));
            }

            if (index == 0) {
                System.out.println("Vehicle " + vehId);
                System.out.println("Route : " + ID.get(parent) + " -> " + ID.get(index));
                System.out.println("Cost : " + calcDist(parent, index));
                cost.add(calcDist(parent, index));
                vehCapc = vehCapc + customerDemand.get(index);
                System.out.println("Capacity of Vehicle " + vehId + " : " + vehCapc + "\n");

                double costTemp = 0;
                for (int i = 0; i < cost.size(); i++) {
                    costTemp = costTemp + cost.get(i);
                }

                unvisitedNodes(visited);
                tourCost = tourCost + costTemp;
                System.out.println("Total Cost of Vehicle " + vehId + " : " + costTemp + "\n");
//                route.add(0);
                currCapc = C;
                vehCapc = 0;
                cost.clear();
                visited.clear();
                ++vehId;
                break;
            }
        }
        while (!x.isEmpty() || !y.isEmpty()) {
            parent = 0;
            outdeg = 0;

            if (x.size() == 1) {
                break;
            }
            while (currCapc != 0 && currCapc > 0) {
//                ArrayList visited = new ArrayList(); 
                index = lowCost(parent, outdeg, currCapc, customerDemand);
//                currCapc = currCapc - customerDemand.get(parent);
                double currDist = calcDist(parent, index);
                distance.add(currDist);
//                route.add(index);
                visited.add(index);

                if (currCapc > customerDemand.get(index)) {
                    currCapc = currCapc - customerDemand.get(index);
                    System.out.println("Vehicle " + vehId);
                    System.out.println("Route : " + ID.get(parent) + " -> " + ID.get(index));
                    System.out.println("Cost : " + calcDist(parent, index));
                    cost.add(calcDist(parent, index));
                    vehCapc = vehCapc + customerDemand.get(index);
                    System.out.println("Capacity of Vehicle " + vehId + " : " + vehCapc + "\n");
                    outdeg = parent;
                    parent = index;
                    index = lowCost(parent, outdeg, currCapc, customerDemand);
                    distance.add(calcDist(parent, index));

                }

                if (index == 0) {
                    System.out.println("Vehicle " + vehId);
                    System.out.println("Route : " + ID.get(parent) + " -> " + ID.get(index));
                    System.out.println("Cost : " + calcDist(parent, index));
                    cost.add(calcDist(parent, index));
                    vehCapc = vehCapc + customerDemand.get(index);
                    System.out.println("Capacity of Vehicle " + vehId + " : " + vehCapc + "\n");

                    double costTemp = 0;
                    for (int i = 0; i < cost.size(); i++) {
                        costTemp = costTemp + cost.get(i);
                    }
                    tourCost = tourCost + costTemp;
                    unvisitedNodes(visited);
                    System.out.println("Total Cost of Vehicle " + vehId + " : " + costTemp + "\n");
//                route.add(0);
                    currCapc = C;
                    vehCapc = 0;
                    cost.clear();
                    visited.clear();
                    ++vehId;
                    break;
                }
            }

        }
        System.out.println();
        System.out.println("Tour Cost : " + tourCost);

    }

    public void unvisitedNodes(ArrayList visited) {
        visited.sort(Comparator.naturalOrder());
        for (int i = visited.size() - 1; i >= 0; i--) {
            int temp = (int) visited.get(i);
            x.remove(temp);
            y.remove(temp);
            customerDemand.remove(temp);
            ID.remove(temp);
        }
    }

//    public void printResult(){
//        String route = "";
//        for (int i = 0; i < this.route.size(); i++){
//            route = route + " -> " + this.route.get(i) ;
//        }
//        System.out.println("A* Algorithm");
//        System.out.println("Tour : ");
//        for (int i = 1; i <= vehId; i++){
//            System.out.println("Vehicle " + i);
//        }
//    }

}
