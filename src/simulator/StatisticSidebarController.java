package simulator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

public class StatisticSidebarController {
    @FXML private LineChart<Number,Number> statisticsLineChart;
    @FXML private BarChart<String,Integer> mostPopularGenotypeBarChart;
    @FXML private Pane followPane;



    public StatisticSidebarController(){
        Platform.runLater(this::init);
    }

    private void init(){
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        for (int i = 0; i < 8; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i),2));
        }

        mostPopularGenotypeBarChart.getData().add(series);


    }

    public void addNewDayData(int day,int numberOfAnimals, int numberOfPlants, int avgAnimalEnergy, int animalLiveExpectancy, int numberOfChildren){

    }

    public void setMostPopularGenotype(int[] genotype){

    }

    public void addFollowNode(Node followNode){
        followPane.getChildren().clear();
        followPane.getChildren().add(followNode);
    }
}
