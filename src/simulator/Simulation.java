package simulator;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

import java.util.LinkedList;

public class Simulation extends Thread{
    private final SimulationProperties properties;
    private final StatisticSidebarController sidebarController;
    private final FollowAnimalController followAnimalController;
    private final Canvas mapCanvas;
    private final WorldMap map;
    private boolean isPaused;
    private boolean hasEnded;

    Simulation(SimulationProperties properties, Canvas mapCanvas, StatisticSidebarController sidebarController, FollowAnimalController followAnimalController){
        this.properties = properties;
        this.sidebarController = sidebarController;
        this.followAnimalController = followAnimalController;
        this.mapCanvas = mapCanvas;
        this.map = prepareMap();




    }

    private WorldMap prepareMap(){
        int jungleX1 = (properties.mapWidth - properties.jungleWidth) / 2;
        int jungleY1 = (properties.mapHeight - properties.jungleHeight) / 2;
        Vector2d jungleLowerLeftCorner = new Vector2d(jungleX1,jungleY1);

        int jungleX2 = (properties.mapWidth + properties.jungleWidth) / 2;
        int jungleY2 = (properties.mapHeight + properties.jungleHeight) / 2;
        Vector2d jungleUpperRightCorner = new Vector2d(jungleX2,jungleY2);

        Vector2d mapDimensions = new Vector2d(properties.mapWidth,properties.mapHeight);

        WorldMap map = new WorldMap(mapDimensions,jungleLowerLeftCorner,jungleUpperRightCorner);


        for (int i = 0; i < properties.startAnimalsNumber; i++) {
            Vector2d position = new Vector2d(properties.mapWidth/2,properties.mapHeight/2);
            Animal animal = new Animal(map,position,properties.animalStartEnergy,properties.animalMoveEnergy);
            map.addAnimal(animal);
        }

        for (int i = 0; i < properties.plantsPerDayInsideJungle; i++) {
            map.addPlantInsideJungle(properties.energyFromPlant);
        }
        for (int i = 0; i < properties.plantsPerDayOutsideJungle; i++) {
            map.addPlantOutsideJungle(properties.energyFromPlant);
        }

        return map;
    }

    public void simulateADay(){
        map.removeDeadAnimals();
        map.moveAnimals();
        map.eatPlants();
        map.reproduceAnimals(properties.minEnergyForReproduction);

        for (int i = 0; i < properties.plantsPerDayInsideJungle; i++) {
            map.addPlantInsideJungle(properties.energyFromPlant);
        }
        for (int i = 0; i < properties.plantsPerDayOutsideJungle; i++) {
            map.addPlantOutsideJungle(properties.energyFromPlant);
        }

    }

    public void displayCurrentState(){
        map.drawMap(mapCanvas);
    }

    @Override
    public synchronized void start() {
        Platform.runLater(this::displayCurrentState);
        isPaused = false;
        hasEnded = false;
        super.start();

    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(!isInterrupted() && !hasEnded) {
            if(!isPaused)simulateADay();
            Platform.runLater(this::displayCurrentState);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void pause(){
        isPaused = true;
    }

    public void unPause(){
        isPaused = false;
    }
    public void end(){
        this.hasEnded = true;
    }
}
