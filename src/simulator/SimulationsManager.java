package simulator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;


import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulationsManager {
    private final SimulationProperties simulationProperties;
    private final LinkedList<Simulation> simulations;
    private ScheduledExecutorService scheduler;



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

        FXMLLoader followAnimalLoader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/follow_animal_sidebar.fxml"));
        Node followAnimalSidebarNode = followAnimalLoader.load();
        FollowAnimalController followAnimalController = followAnimalLoader.getController();
        pane.setRight(followAnimalSidebarNode);


        Canvas mapCanvas = new Canvas();
        CanvasPane canvasPane = new CanvasPane(mapCanvas);
        pane.setCenter(canvasPane);



        Simulation simulation = new Simulation(simulationProperties,mapCanvas,statisticSidebarController,followAnimalController);
        simulations.add(simulation);
        return pane;
    }

    public void startSimulations(){
        scheduler = Executors.newScheduledThreadPool(simulations.size());
        simulations.forEach(simulation -> scheduler.scheduleWithFixedDelay(simulation,10,50, TimeUnit.MILLISECONDS));
    }

    public void stopSimulations(){
        scheduler.shutdown();

    }

}
