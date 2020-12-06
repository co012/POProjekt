package simulator;

import javafx.scene.canvas.Canvas;

import java.util.LinkedList;

public class Simulation {
    private final SimulationProperties properties;
    private final StatisticSidebarController sidebarController;
    private final FollowAnimalController followAnimalController;
    private final Canvas mapCanvas;

    Simulation(SimulationProperties properties, Canvas mapCanvas, StatisticSidebarController sidebarController, FollowAnimalController followAnimalController){
        this.properties = properties;
        this.sidebarController = sidebarController;
        this.followAnimalController = followAnimalController;
        this.mapCanvas = mapCanvas;
    }

    public void simulateADay(){

    }

    public void drawCurrentState(){

    }
}
