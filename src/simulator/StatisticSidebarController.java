package simulator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;

public class StatisticSidebarController {
    @FXML private LineChart<String,Integer> statisticsLineChart;
    @FXML private BarChart<String,Integer> mostPopularGenotypeBarChart;
    @FXML private Pane followPane;
    private final XYChart.Series<String,Integer> animalsNumberSeries;
    private final XYChart.Series<String,Integer> plantsNumberSeries;



    public StatisticSidebarController(){
        animalsNumberSeries = new XYChart.Series<>();
        plantsNumberSeries = new XYChart.Series<>();
        Platform.runLater(this::init);
    }

    private void init(){
        XYChart.Series<String,Integer> series = new XYChart.Series<>();
        for (int i = 0; i < 8; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i),2));
        }
        mostPopularGenotypeBarChart.getData().add(series);

        statisticsLineChart.getData().add(animalsNumberSeries);
        statisticsLineChart.getData().add(plantsNumberSeries);


    }

    public void addNewDayData(int day,int numberOfAnimals, int numberOfPlants, int avgAnimalEnergy, int animalLiveExpectancy, int numberOfChildren){

    }

    public void addNewAnimalNumberOnDayData(int animalsNumber,int day){
        animalsNumberSeries.getData().add(new XYChart.Data<>(String.valueOf(day),animalsNumber));
    }

    public void addNewPlantsNumberOnDayData(int plantsNumber, int day){
        plantsNumberSeries.getData().add(new XYChart.Data<>(String.valueOf(day),plantsNumber));
    }

    public void setMostPopularGenotype(int[] genotype){

    }

    public void addFollowNode(Node followNode){
        followPane.getChildren().clear();
        followPane.getChildren().add(followNode);
    }
}
