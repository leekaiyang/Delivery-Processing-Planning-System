package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    static ArrayList<Location> location = new ArrayList<Location>();
    static int N, C;
    static int[] vertex = null;
    static File file;

    FileChooser fileChooser = new FileChooser();

    @FXML
    private TextArea textArea;

    @FXML
    private Button basicSimulation;

    @FXML
    private Button greedySimulation;

    @FXML
    private Button MCTS;

    @FXML
    private Button aStar;

    @FXML
    private Button customerDetails;

//    @FXML
//    private Button showDistance;

//    @FXML
//    private TextArea textOutput;

    @FXML
    private Slider sliderMCTSTime;

    @FXML
    private Text showMCTSTime;


    @FXML
    private TextArea textAreaUI;

    static TextArea staticTxtArea;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticTxtArea = textAreaUI;
        fileChooser.setInitialDirectory(new File("C:\\"));
    }

    @FXML
    void getTextFile(ActionEvent event) throws Exception{
        file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            N = sc.nextInt();
            C = sc.nextInt();
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
    }

    @FXML
    void setTime(MouseEvent event) {
        showMCTSTime.setText("Time: " + String.valueOf((int)sliderMCTSTime.getValue()) + " sec");
    }

    @FXML
    void runCustomerDetails(ActionEvent event) throws Exception{
        System.out.println(location.get(0));
        System.out.println("~~~~~~~~~~~~~~~~ Customers Details ~~~~~~~~~~~~~~~~~");
        for (int i = 1; i < location.size(); i++) {
            System.out.println(location.get(i));
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

//    @FXML
//    void runDistanceBetweenAdjacencyVertex(ActionEvent event) throws Exception{
//        BasicSimulation basicSimulation = new BasicSimulation(location);
//        basicSimulation.printDistanceOfAdjacentVertex();
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
//    }

    @FXML
    void runBasicSimulation(ActionEvent event) throws Exception{
        BasicSimulation basicSimulation = new BasicSimulation(location);
        basicSimulation.bfsBreathFirstSearch();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

    @FXML
    void runGreedySimulation(ActionEvent event) throws Exception{
        GreedySearch greedySearch = new GreedySearch();
        greedySearch.solve(file);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

    @FXML
    void runAStar(ActionEvent event) throws Exception{
        aStar a = new aStar();
        a.route(file);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

    @FXML
    void runMCTS(ActionEvent event) throws Exception{
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
        int time = (int)sliderMCTSTime.getValue();
        MCTSSimulation<Integer> mctsSimulation = new MCTSSimulation(graph, C, time);
        System.out.println(graph.totalCost(mctsSimulation.pathSearch));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }


}
