package simulator.input;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulator.custom_controls.NumberField;
import simulator.SimulationProperties;

import java.io.IOException;

public class InputWindow extends Stage {

    @FXML
    NumberField mapWidthNumberField;
    @FXML NumberField mapHeightNumberField;
    @FXML NumberField startEnergyNumberField;
    @FXML NumberField moveEnergyNumberField;
    @FXML NumberField plantEnergyNumberField;
    @FXML NumberField jungleWidthNumberField;
    @FXML NumberField jungleHeightNumberField;
    @FXML NumberField startAnimalsNumberNumberField;
    @FXML NumberField plantsPerDayInsideJungleNumberField;
    @FXML NumberField plantsPerDayOutsideJungleNumberField;
    @FXML NumberField minEnergyForReproductionNumberField;
    @FXML Button okButton;
    @FXML GridPane mainGridPane;

    private SimulationProperties properties;

    public InputWindow() throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/input_window.fxml"));
        loader.setController(this);
        loader.load();
        this.setTitle("Enter evolution simulation parameters");
        this.setAlwaysOnTop(true);
        this.setScene(new Scene(loader.getRoot(),400,400));
        


    }

    public SimulationProperties getSimulationProperties(){
        this.showAndWait();
        return properties;
    }

    @FXML
    private void onOkButtonClicked(){
        for(Node node : mainGridPane.getChildren()){
            if(node instanceof TextField){
                TextField textField = (TextField) node;
                if("".equals(textField.getText())){

                    return;
                }
            }
        }

        int mapWidth = mapWidthNumberField.getInt();
        int mapHeight = mapHeightNumberField.getInt();
        int animalStartEnergy = startEnergyNumberField.getInt();
        int animalMoveEnergy= moveEnergyNumberField.getInt();
        int energyFromPlant = plantEnergyNumberField.getInt();
        int jungleWidth = jungleWidthNumberField.getInt();
        int jungleHeight = jungleHeightNumberField.getInt();
        int startAnimalsNumber = startAnimalsNumberNumberField.getInt();
        int plantsPerDayInsideJungle = plantsPerDayInsideJungleNumberField.getInt();
        int plantsPerDayOutsideJungle = plantsPerDayOutsideJungleNumberField.getInt();
        int minEnergyForReproduction = minEnergyForReproductionNumberField.getInt();

        properties = new SimulationProperties(
                mapWidth,
                mapHeight,
                animalStartEnergy,
                animalMoveEnergy,
                energyFromPlant,
                jungleWidth,
                jungleHeight,
                startAnimalsNumber,
                plantsPerDayInsideJungle,
                plantsPerDayOutsideJungle,
                minEnergyForReproduction
        );

        close();
    }


}
