package simulator;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SimulationPropertiesLoader {
    private final FileChooser fileChooser;

    private final static String MAP_WIDTH = "width";
    private final static String MAP_HEIGHT = "height";
    private final static String START_ENERGY = "startEnergy";
    private final static String MOVE_ENERGY = "moveEnergy";
    private final static String PLANT_ENERGY = "plantEnergy";
    private final static String JUNGLE_WIDTH = "jungleWidth";
    private final static String JUNGLE_HEIGHT = "jungleHeight";
    private final static String START_ANIMAL_NUMBER = "startAnimalsNumber";
    private final static String PLANTS_PER_DAY = "plantsPerDay";
    private final static String MIN_ENERGY_FOR_REPRODUCTION = "minEnergyForReproduction";

    public SimulationPropertiesLoader(){
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("*.json", "*.json");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setTitle("Select simulation properties file");
    }


//TODO: Use Optional
    public SimulationProperties loadFromJsonFile(Window window) throws FileNotFoundException,ClassCastException,NullPointerException {

        File file = fileChooser.showOpenDialog(window);
        if(file == null) return null;

        JsonReader reader = Json.createReader(new FileInputStream(file));
        JsonObject jsonObject = reader.readObject();

        int mapWidth = jsonObject.getInt(MAP_WIDTH);
        int mapHeight = jsonObject.getInt(MAP_HEIGHT);
        int animalStartEnergy = jsonObject.getInt(START_ENERGY);
        int animalMoveEnergy= jsonObject.getInt(MOVE_ENERGY);
        int energyFromPlant = jsonObject.getInt(PLANT_ENERGY);
        int jungleWidth = jsonObject.getInt(JUNGLE_WIDTH);
        int jungleHeight = jsonObject.getInt(JUNGLE_HEIGHT);
        int startAnimalsNumber = jsonObject.getInt(START_ANIMAL_NUMBER);
        int plantsPerDay = jsonObject.getInt(PLANTS_PER_DAY);
        int minEnergyForReproduction = jsonObject.getInt(MIN_ENERGY_FOR_REPRODUCTION);


        return new SimulationProperties(mapWidth,
                mapHeight,
                animalStartEnergy,
                animalMoveEnergy,
                energyFromPlant,
                jungleWidth,
                jungleHeight,
                startAnimalsNumber,
                plantsPerDay,
                minEnergyForReproduction);
    }
}
