package simulator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;


import java.io.IOException;
import java.util.LinkedList;

public class SimulationsManager {
    private final SimulationProperties simulationProperties;
    private final LinkedList<Simulation> simulations;



    public SimulationsManager(SimulationProperties simulationProperties) {
        this.simulationProperties = simulationProperties;
        this.simulations = new LinkedList<>();
    }

    public BorderPane createNewSimulation() throws IOException {
        BorderPane pane = new BorderPane();


        FXMLLoader statisticSidebarLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/statistic_sidebar.fxml"));
        Node statisticSidebarNode = statisticSidebarLoader.load();
        StatisticSidebarController statisticSidebarController = statisticSidebarLoader.getController();
        pane.setLeft(statisticSidebarNode);

        FXMLLoader followAnimalLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/follow_animal_scene.fxml"));
        Node followAnimalNode = followAnimalLoader.load();
        FollowAnimalController followAnimalController = followAnimalLoader.getController();
        statisticSidebarController.addFollowNode(followAnimalNode);


        Canvas mapCanvas = new Canvas();
        CanvasPane canvasPane = new CanvasPane(mapCanvas);
        pane.setCenter(canvasPane);



        Simulation simulation = new Simulation(simulationProperties,mapCanvas,statisticSidebarController,followAnimalController);
        simulations.add(simulation);
        return pane;
    }

    public void startSimulations(){
        simulations.forEach(Simulation::start);
    }

    public void stopSimulations(){
        simulations.forEach(Simulation::end);
    }

}
