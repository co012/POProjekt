package simulator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;


import java.io.FileNotFoundException;
import java.io.IOException;

public class AppController {
    @FXML private CheckMenuItem runTwoMapsCheckbox;
    @FXML private Label mapWidthLabel;
    @FXML private Label mapHeightLabel;
    @FXML private Label animalStartEnergyLabel;
    @FXML private Label animalMoveEnergyLabel;
    @FXML private Label energyFromPlantLabel;
    @FXML private Label jungleWidthLabel;
    @FXML private Label jungleHeightLabel;
    @FXML private Label startAnimalsNumberLabel;
    @FXML private Label plantsPerDayLabel;
    @FXML private Label minEnergyForReproductionLabel;
    @FXML private Button startButton;
    @FXML private SplitPane contentSplitPane;

    private Stage stage;
    private SimulationsManager simulationsManager;
    private SimulationProperties simulationProperties;



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

        simulationProperties = properties;
        startButton.setDisable(false);
    }

    @FXML
    private void onStartButtonClicked(){
        if(startButton.isDisabled())return;

        contentSplitPane.getItems().clear();
        simulationsManager = new SimulationsManager(simulationProperties);
        if (!addSimulation()) return;
        if(runTwoMapsCheckbox.isSelected()){
            if (!addSimulation()) return;
        }


    }

    private boolean addSimulation() {
        try {
            BorderPane simulationBorderPane = simulationsManager.createNewSimulation();
            contentSplitPane.getItems().add(simulationBorderPane);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
