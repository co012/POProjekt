package simulator;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

import java.util.LinkedList;
import java.util.Optional;

public class Simulation implements IPauseEventHandler,Runnable{
    private final SimulationProperties properties;
    private final StatisticSidebarController sidebarController;
    private final FollowAnimalController followAnimalController;
    private final Canvas mapCanvas;
    private final WorldMap map;
    private volatile boolean isPaused;
    private int day;

    Simulation(SimulationProperties properties, Canvas mapCanvas, StatisticSidebarController sidebarController, FollowAnimalController followAnimalController) {
        this.properties = properties;
        this.sidebarController = sidebarController;
        this.followAnimalController = followAnimalController;
        this.mapCanvas = mapCanvas;
        this.map = prepareMap();
        day = 0;

        mapCanvas.setOnMouseClicked(e ->{
            if(!isPaused)return;

            int x = (int) ((e.getX()/mapCanvas.getWidth()) * properties.mapWidth);
            int y = (int) ((e.getY()/mapCanvas.getHeight()) * properties.mapHeight);
            Vector2d field = new Vector2d(x,y);
            onMapFieldClicked(field);

        });

        followAnimalController.setOnPauseEventHandler(this);

        followAnimalController.setOnSelectMostPopularEventHandler(() -> {
                map.selectAnimalsWithMostPopularGenotype();
                map.drawMap(mapCanvas);
        });

    }

    private void onMapFieldClicked(Vector2d field){
        LinkedList<Animal> animalsOnField = map.getAnimalsOnField(field);
        followAnimalController.viewAnimals(animalsOnField);

    }

    private WorldMap prepareMap() {
        int jungleX1 = (properties.mapWidth - properties.jungleWidth) / 2;
        int jungleY1 = (properties.mapHeight - properties.jungleHeight) / 2;
        Vector2d jungleLowerLeftCorner = new Vector2d(jungleX1, jungleY1);

        int jungleX2 = (properties.mapWidth + properties.jungleWidth) / 2;
        int jungleY2 = (properties.mapHeight + properties.jungleHeight) / 2;
        Vector2d jungleUpperRightCorner = new Vector2d(jungleX2, jungleY2);

        Vector2d mapDimensions = new Vector2d(properties.mapWidth, properties.mapHeight);

        WorldMap map = new WorldMap(mapDimensions, jungleLowerLeftCorner, jungleUpperRightCorner);


        map.spawnAnimals(properties.startAnimalsNumber, properties.animalStartEnergy, properties.animalMoveEnergy);
        map.spawnPlantsInsideJungle(properties.plantsPerDayInsideJungle, properties.energyFromPlant);
        map.spawnPlantsOutsideJungle(properties.plantsPerDayOutsideJungle, properties.energyFromPlant);

        return map;
    }

    public void simulateADay() {
        synchronized (map){
            day++;
            map.beginDay();
            map.removeDeadAnimals();
            map.moveAnimals();
            map.eatPlants();
            map.reproduceAnimals(properties.minEnergyForReproduction);
            map.spawnPlantsInsideJungle(properties.plantsPerDayInsideJungle, properties.energyFromPlant);
            map.spawnPlantsOutsideJungle(properties.plantsPerDayOutsideJungle, properties.energyFromPlant);
        }



    }

    public void drawMap() {
        synchronized (map){
            map.drawMap(mapCanvas);
        }


    }

    public void updateStatistics(){
        synchronized (map){
            sidebarController.addNewAnimalNumberOnDayData(map.getAnimalsNumber(), day);
            sidebarController.addNewPlantsNumberOnDayData(map.getPlantsNumber(), day);
            sidebarController.addNewAvgEnergyOnDayData(map.getAvgEnergy(), day);
            sidebarController.addNewBirthRateOnDayData(map.getAvgChildrenNumber(), day);
            sidebarController.addNewLifeExpectancyOnDayData(map.getLifeExpectancy(), day);
            Optional<Genotype> genotypeOptional = map.getMostPopularGenotype();
            genotypeOptional.ifPresent(sidebarController::setMostPopularGenotype);
            followAnimalController.updateFollowStatistics(day);
        }


    }



    @Override
    public void run() {
        Platform.runLater(this::drawMap);
        if (!isPaused){
            simulateADay();
            Platform.runLater(this::updateStatistics);
        }

    }


    public void onPause() {
        isPaused = true;
    }

    public void onUnPause() {
        isPaused = false;
        map.unSelectAllAnimals();
    }



}
