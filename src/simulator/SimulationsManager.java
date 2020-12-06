package simulator;

import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.LinkedList;

public class SimulationsManager {
    private final SimulationProperties simulationProperties;
    private final LinkedList<Simulation> simulations;


    public SimulationsManager(SimulationProperties simulationProperties, LinkedList<Simulation> simulations) {
        this.simulationProperties = simulationProperties;
        this.simulations = simulations;
    }

    public void addNewSimulation(Pane pane){

    }
}
