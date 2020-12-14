package simulator;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class StatisticSidebarController {
    private final static int MAX_DATA_LENGTH = 100;

    @FXML private LineChart<String,Integer> firstStatisticsLineChart;
    @FXML private LineChart<String,Double> secondStatisticsLineChart;
    @FXML private LineChart<String,Double> thirdStatisticsLineChart;
    @FXML private BarChart<String,Integer> mostPopularGenotypeBarChart;
    private final XYChart.Series<String,Integer> animalsNumberSeries;
    private final XYChart.Series<String,Integer> plantsNumberSeries;
    private final XYChart.Series<String,Double> avgEnergySeries;
    private final XYChart.Series<String,Double> lifeExpectancySeries;
    private final XYChart.Series<String,Double> birthRateSeries;
    private final XYChart.Series<String, Integer> mostPopularGenotypeSeries;





    public StatisticSidebarController(){
        animalsNumberSeries = new XYChart.Series<>();
        animalsNumberSeries.setName("Animals number");
        plantsNumberSeries = new XYChart.Series<>();
        plantsNumberSeries.setName("Plants number");
        avgEnergySeries = new XYChart.Series<>();
        avgEnergySeries.setName("Average energy");
        lifeExpectancySeries = new XYChart.Series<>();
        lifeExpectancySeries.setName("Life expectancy");
        birthRateSeries = new XYChart.Series<>();
        birthRateSeries.setName("Birth rate");
        mostPopularGenotypeSeries = new XYChart.Series<>();
        for (int i = 0; i < Genotype.GENS_TYPES_NUMBER; i++) {
            mostPopularGenotypeSeries.getData().add(new XYChart.Data<>(String.valueOf(i),i));
        }
        Platform.runLater(this::init);
    }

    private void init(){
        mostPopularGenotypeBarChart.getData().add(mostPopularGenotypeSeries);

        firstStatisticsLineChart.getData().add(animalsNumberSeries);
        firstStatisticsLineChart.getData().add(plantsNumberSeries);

        secondStatisticsLineChart.getData().add(birthRateSeries);

        thirdStatisticsLineChart.getData().add(avgEnergySeries);
        thirdStatisticsLineChart.getData().add(lifeExpectancySeries);


    }

    public void addNewAnimalNumberOnDayData(int animalsNumber,int day){
        addIntegerDataItem(animalsNumberSeries.getData(),String.valueOf(day),animalsNumber);

    }

    public void addNewPlantsNumberOnDayData(int plantsNumber, int day){
        addIntegerDataItem(plantsNumberSeries.getData(),String.valueOf(day),plantsNumber);
    }

    public void addNewAvgEnergyOnDayData(double avgEnergy, int day){
        addDoubleDataItem(avgEnergySeries.getData(), String.valueOf(day),avgEnergy);
    }

    public void addNewLifeExpectancyOnDayData(double liveExpectancy, int day){
        addDoubleDataItem(lifeExpectancySeries.getData(), String.valueOf(day),liveExpectancy);
    }

    public void addNewBirthRateOnDayData(double birtRate, int day){
        addDoubleDataItem(birthRateSeries.getData(), String.valueOf(day),birtRate);
    }

    private void addIntegerDataItem(ObservableList<XYChart.Data<String,Integer>> dataList,String category,Integer number){
        dataList.add(new XYChart.Data<>(category,number));
        if(dataList.size() > MAX_DATA_LENGTH){
            dataList.remove(0);
        }
    }

    private void addDoubleDataItem(ObservableList<XYChart.Data<String,Double>> dataList,String category,Double number){
        dataList.add(new XYChart.Data<>(category,number));
        if(dataList.size() > MAX_DATA_LENGTH){
            dataList.remove(0);
        }
    }

    public void setMostPopularGenotype(Genotype genotype){
        int[] genotype_array = genotype.getGeneFrequencyArray();
        mostPopularGenotypeSeries.getData().forEach( data -> {
            int i = Integer.parseInt(data.getXValue());
            data.setYValue(genotype_array[i]);
        });
    }

}
