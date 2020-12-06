package simulator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.stage.*;


import java.io.FileNotFoundException;
import java.io.IOException;

public class AppController {
    public CheckMenuItem runTwoMapsCheckbox;
    public Label mapWidthLabel;
    public Label mapHeightLabel;
    public Label animalStartEnergyLabel;
    public Label animalMoveEnergyLabel;
    public Label energyFromPlantLabel;
    public Label jungleWidthLabel;
    public Label jungleHeightLabel;
    public Label startAnimalsNumberLabel;
    public Label plantsPerDayLabel;
    public Label minEnergyForReproductionLabel;
    public Button startButton;

    private Stage stage;



    public void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    @FXML
    private void onLoadFromFileClicked(){

        SimulationPropertiesLoader propertiesLoader = new SimulationPropertiesLoader();

        try {
            SimulationProperties properties = propertiesLoader.loadFromJsonFile(stage);
            prepareForSimulation(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("File error");
            alert.setContentText(e.getMessage());
            alert.show();

        }


    }

    @FXML
    private void onLoadDirectlyClicked(){

        try {
            InputWindow inputWindow = new InputWindow();
            inputWindow.initOwner(stage);
            inputWindow.initModality(Modality.WINDOW_MODAL);
            prepareForSimulation(inputWindow.getSimulationProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareForSimulation(SimulationProperties properties){
        if(properties == null) return;
        mapWidthLabel.setText(String.valueOf(properties.mapWidth));
        mapHeightLabel.setText(String.valueOf(properties.mapHeight));
        animalStartEnergyLabel.setText(String.valueOf(properties.animalStartEnergy));
        animalMoveEnergyLabel.setText(String.valueOf(properties.animalMoveEnergy));
        energyFromPlantLabel.setText(String.valueOf(properties.energyFromPlant));
        jungleWidthLabel.setText(String.valueOf(properties.jungleWidth));
        jungleHeightLabel.setText(String.valueOf(properties.jungleHeight));
        startAnimalsNumberLabel.setText(String.valueOf(properties.startAnimalsNumber));
        plantsPerDayLabel.setText(String.valueOf(properties.plantsPerDay));
        minEnergyForReproductionLabel.setText(String.valueOf(properties.minEnergyForReproduction));

        if(!properties.areValid()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Data is self-contradictory");
            alert.show();
            startButton.setDisable(true);
            return;
        }

        startButton.setDisable(false);
    }

    @FXML
    private void onStartButtonClicked(){
        if(startButton.isDisabled())return;



    }
}
