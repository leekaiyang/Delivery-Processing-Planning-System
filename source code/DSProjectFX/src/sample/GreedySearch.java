package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;


//Author: Tianyi Liu
//8 Jun 2021
//Ver 1.3.10

public class GreedySearch {
    int N,C,index,count = 0, currentCapacity, IDCount = 0,lastLocationID, nextLocationID,locationID = 0, demandStartIndex, demandEndIndex, lastDemand;
    double costTemp, lastCost;
    //number of customers, capacity of the vehicle, index number
    //counter for the number of shipments in one vehicle, current capacity of the vehicle, ID counter

    //The following Customer,Depot are sorted by index order
    ArrayList<Integer> x = new ArrayList<>();											//store x coordinates
    ArrayList<Integer> y = new ArrayList<>();											//store y coordinates
    ArrayList<Integer> ID = new ArrayList<>();											//store depot and customer ID
    ArrayList<Integer> customerDemand = new ArrayList<>();								//store demand of each customer
    ArrayList<Integer> remainCustomerDemand = new ArrayList<>();						//store current demand of each customer
    ArrayList<Double> distance = new ArrayList<>();										//store distance
    ArrayList<Double> cost = new ArrayList<>(); 										//store cost

    public double getDistanceD(int j, int k) {
        //calculate distance method
        double distance = Math.sqrt ((x.get(j) - x.get(k) ) * ( x.get(j) - x.get(k) ) + ( y.get(j) - y.get(k) ) * ( y.get(j) - y.get(k)));
        return distance;
    }


    public int minIndex (ArrayList<Integer> list) {
        //To find the index of the smallest object in the arraylist
        if (list.size() != 1 && !list.isEmpty()){
            list.remove(0);
            int index = list.indexOf(Collections.min(list));
            list.add(0,0);
            return index+1;
        } else
            //whether the arraylist still has the smallest object available to determine
            return -1;
        //If return -1, break the loop
    }

    public void readData(File filename) throws IOException {
        //to read txt data
        String str, num;
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        num = in.readLine();								//read first line
        String[] numOfC=num.split(" ");				//spilt the string
        N = Integer.parseInt(numOfC[0]); 					//number of customers
        C = Integer.parseInt(numOfC[1]); 					//capacity of the vehicle
        while((str = in.readLine())!=null){
            String[] spiltDataString; 						//spilt the string
            spiltDataString = str.split(" ");
            x.add(Integer.valueOf(spiltDataString[0])); 	//add x coordinates to arraylist
            y.add(Integer.valueOf(spiltDataString[1])); 	//add x coordinates to arraylist
            customerDemand.add(Integer.valueOf(spiltDataString[2])); 			//add customer demand to arraylist
            remainCustomerDemand.add(Integer.valueOf(spiltDataString[2])); 		//add current customer demand to arraylist
            ID.add(IDCount); 								//add customer ID
            ++IDCount; 										//index starts from 0 not from 1
        }
        in.close();
    }

    public void solve(File filename) throws IOException {
        //algorithm body

        readData(filename);
        System.out.println("Greedy Simulation");
        System.out.println("Tour\n");
        currentCapacity = C;
        int vehicleAmount = 1;

        do {
            if (minIndex(customerDemand) == -1) 										//return -1, the arraylist do not have the smallest object
                break;

            while (currentCapacity != 0){ 												//while vehicle have room to continue

                if (minIndex(customerDemand) == -1) 									//return -1, the arraylist do not have the smallest object
                    break;

                index = minIndex(customerDemand); 										//get the index of the least amount of customer demand
                nextLocationID = index;													//assign the index to the next position id
                double currentDistance = getDistanceD(locationID,nextLocationID); 		//get distance from current location to next location
                distance.add(currentDistance); 											//add distance to arraylist

                if (currentCapacity > customerDemand.get(nextLocationID)){				//if current vehicle capacity is larger than demand

                    currentCapacity = currentCapacity - (customerDemand.get(nextLocationID));//calculate the remain capacity of vehicle
                    distance.add(getDistanceD(locationID,ID.get(index)));

                    //print output
                    System.out.println("Vehicle " + vehicleAmount);
                    System.out.println("Route: " + locationID + " -> " + ID.get(index));
                    System.out.println("Cost: " + getDistanceD(locationID,ID.get(index)));
                    cost.add(getDistanceD(locationID,ID.get(index)));
                    System.out.println(("Capacity: " + (customerDemand.get(nextLocationID))));
                    System.out.println();

                    demandEndIndex = cost.indexOf((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
//                    demandStartIndex = demandEndIndex+1;
                    lastCost = getDistanceD(locationID,ID.get(index));
                    lastLocationID = ID.get(index);
                    locationID = ID.get(index); 										//move current location to next location
                    lastDemand = customerDemand.get(nextLocationID);

                    customerDemand.remove(nextLocationID);								//remove current customer's demand in arraylist
                    ID.remove(nextLocationID); 											//remove current location from ID list
                    count = 0; 															//counter for the number of shipments in one vehicle

                } else if (currentCapacity < customerDemand.get(nextLocationID) && count > 0){
                    //if current vehicle capacity is smaller than demand and vehicle have come to the same location at least once

                    customerDemand.set(nextLocationID, ( (customerDemand.get (nextLocationID) ) - currentCapacity) );
                    //change the current customer demand to the demand after subtracting the vehicle capacity
                    distance.add(getDistanceD(locationID,nextLocationID)+getDistanceD(locationID,0));
                    //calculate the distance back to the depot (location ID=0) and add to arraylist
                    currentCapacity = C; 												//reset current vehicle capacity


                    //print output
                    System.out.println("Vehicle " + vehicleAmount);
                    System.out.println("Route: " + "0"+ " -> " + ID.get(index) + " -> 0");
                    System.out.println("Cost: " + (getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    cost.add((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    demandEndIndex = cost.indexOf((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    System.out.println(("Total Capacity of Vehicle " + vehicleAmount +" : " + currentCapacity));
                    costTemp = 0;
                    for (int i = demandEndIndex; i <= demandStartIndex; i++) {
                        costTemp = costTemp + cost.get(i);
                    }
                    System.out.println("Total cost of Vehicle " + vehicleAmount +" : " + costTemp);
                    System.out.println();
                    count++; 															//counter for the number of shipments in one vehicle
                    demandStartIndex = demandEndIndex+2;


                    locationID = 0;														//reset location ID to depot
                    vehicleAmount = vehicleAmount + 1; 									//number of vehicle++

                } else if (currentCapacity < customerDemand.get(nextLocationID) && count == 0){
                    //if current vehicle capacity is smaller than demand and vehicle have come to the same location only once

                    customerDemand.set(nextLocationID, ( (customerDemand.get (nextLocationID) ) - currentCapacity) );
                    //change the current customer demand to the demand after subtracting the vehicle capacity
                    distance.add(getDistanceD(locationID,nextLocationID)+getDistanceD(locationID,0));
                    //calculate the distance back to the depot (location ID=0) and add to arraylist
                    currentCapacity = C;


                    //print output
                    System.out.println("Vehicle " + vehicleAmount);
                    System.out.println("Route: " + lastLocationID + " -> " + ID.get(index) + " -> 0");
                    System.out.println("Cost: " + (getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    cost.add((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    demandEndIndex = cost.indexOf((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    System.out.println(("Total Capacity of Vehicle " + vehicleAmount +" : " + currentCapacity));
                    costTemp = 0;
                    for (int i = demandStartIndex; i <= demandEndIndex; i++) {
                        costTemp = costTemp + cost.get(i);
                    }
                    System.out.println("Total cost of Vehicle " + vehicleAmount +" : " + costTemp);
                    System.out.println();
                    count++; 															//counter for the number of shipments in one vehicle

                    demandStartIndex = demandEndIndex+1;
                    locationID = 0; 													//reset location ID to depot
                    vehicleAmount = vehicleAmount + 1; 									//number of vehicle++

                } else {
                    //if current vehicle capacity equals to customer demand

                    distance.add(getDistanceD(locationID,nextLocationID)+getDistanceD(locationID,0));//calculate the distance back to the depot (location ID=0) and add to arraylist
                    customerDemand.remove(nextLocationID); 								//remove current location
                    currentCapacity = C; 												//reset vehicle capacity


                    //print output
                    System.out.println("Vehicle " + vehicleAmount);
                    System.out.println("Route: " + lastLocationID + " -> " + ID.get(index) + " -> " + "0");
                    System.out.println("Cost: " + (getDistanceD(lastLocationID,ID.get(index))+getDistanceD(ID.get(index),0)));
                    cost.add((getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0)));
                    demandEndIndex = cost.indexOf(getDistanceD(lastLocationID,ID.get(index)) + getDistanceD(ID.get(index),0));
                    System.out.println(("Total Capacity of Vehicle " + vehicleAmount +" : " + currentCapacity));
                    costTemp = 0;
                    for (int i = demandStartIndex; i <= demandEndIndex; i++) {
                        costTemp = costTemp + cost.get(i);
                    }
                    System.out.println("Total cost of Vehicle " + vehicleAmount +" : " + costTemp);
                    System.out.println();

                    locationID = 0; 													//reset location ID
                    lastLocationID = 0;
                    ID.remove(nextLocationID); 											//remove current location
                    count = 0; 															//counter for the number of shipments in one vehicle
                    demandStartIndex = demandEndIndex+1;
                    vehicleAmount = vehicleAmount + 1;									//number of vehicle++
                }

            }
        } while (customerDemand.isEmpty() || minIndex(customerDemand) != -1);
        //if demand arraylist is empty or no available demand in arraylist
        if (lastLocationID != 0){
            System.out.println("Vehicle " + vehicleAmount);
            System.out.println("Route: " + lastLocationID + " -> 0");
            System.out.println("Cost: " + getDistanceD(0,lastLocationID));
            cost.add(getDistanceD(0,lastLocationID));
            System.out.println("Total Capacity of Vehicle " + vehicleAmount +" : " + lastDemand);
            System.out.println("Total cost of Vehicle " + vehicleAmount +" : " + 2 * lastCost);
            double totalCost = 0;
            for (int i = 0; i < cost.size(); i++) { 										//add up all cost in arraylist to calculate cost
                totalCost = totalCost + cost.get(i);
            }
            System.out.println("Tour cost: " + (totalCost)); 						        //print
        } else {
            double totalCost = 0;
            for (int i = 0; i < cost.size(); i++) { 										//add up all cost in arraylist to calculate cost
                totalCost = totalCost + cost.get(i);
            }
            System.out.println("Tour cost: " + totalCost); 									//print
        }

    }
}

