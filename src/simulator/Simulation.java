package simulator;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

public class Simulation extends Thread{
    private final SimulationProperties properties;
    private final StatisticSidebarController sidebarController;
    private final FollowAnimalController followAnimalController;
    private final Canvas mapCanvas;
    private final WorldMap map;
    private volatile boolean isPaused;
    private volatile boolean hasEnded;
    private int day;

    Simulation(SimulationProperties properties, Canvas mapCanvas, StatisticSidebarController sidebarController, FollowAnimalController followAnimalController){
        this.properties = properties;
        this.sidebarController = sidebarController;
        this.followAnimalController = followAnimalController;
        this.mapCanvas = mapCanvas;
        this.map = prepareMap();
        day = 0;




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


        map.spawnAnimals(properties.startAnimalsNumber,properties.animalStartEnergy,properties.animalMoveEnergy);
        map.spawnPlantsInsideJungle(properties.plantsPerDayInsideJungle, properties.energyFromPlant);
        map.spawnPlantsOutsideJungle(properties.plantsPerDayOutsideJungle, properties.energyFromPlant);

        return map;
    }

    public void simulateADay(){
        day++;
        map.removeDeadAnimals();
        map.moveAnimals();
        map.eatPlants();
        map.reproduceAnimals(properties.minEnergyForReproduction);
        map.spawnPlantsInsideJungle(properties.plantsPerDayInsideJungle, properties.energyFromPlant);
        map.spawnPlantsOutsideJungle(properties.plantsPerDayOutsideJungle, properties.energyFromPlant);


    }

    public void displayCurrentState(){
        map.drawMap(mapCanvas);
        sidebarController.addNewAnimalNumberOnDayData(map.getAnimalsNumber(), day);
        sidebarController.addNewPlantsNumberOnDayData(map.getPlantsNumber(), day);
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
