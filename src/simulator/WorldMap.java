package simulator;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {

    private final Vector2d mapDimensions;
    private final Vector2d jungleLowerLeftCorner;
    private final Vector2d jungleUpperRightCorner;
    private final Vector2d jungleDimensions;
    private LinkedList<Animal> animalsList;
    private final LinkedList<Plant> plantsInsideJungleList;
    private final LinkedList<Plant> plantsOutsideJungleList;
    private HashMap<Vector2d, LinkedList<Animal>> animalsHashMap;
    private final Random random;


    public WorldMap(Vector2d mapDimensions, Vector2d jungleLowerLeftCorner, Vector2d jungleUpperRightCorner) {
        this.mapDimensions = mapDimensions;
        this.jungleLowerLeftCorner = jungleLowerLeftCorner;
        this.jungleUpperRightCorner = jungleUpperRightCorner;
        this.jungleDimensions = jungleUpperRightCorner.subtract(jungleLowerLeftCorner);
        animalsList = new LinkedList<>();
        plantsInsideJungleList = new LinkedList<>();
        plantsOutsideJungleList = new LinkedList<>();

        random = new Random();
    }


    public Vector2d getNewPosition(Vector2d oldPosition, MapDirection mapDirection) {
        Vector2d v = oldPosition.add(mapDirection.mapDirectionToVector2d());
        return v.modulo(mapDimensions);
    }

    public Optional<Vector2d> getRandomNeighbourEmptyField(Vector2d vector) {
        Vector2d from = vector.subtract(new Vector2d(1,1));
        Vector2d to = vector.add(new Vector2d(2,2));
        Vector2d field = Vector2d.getRandomVector(from,to);
        for (int i = 0; i < 9; i++) {
            if(!field.equals(vector) && animalsHashMap.get(field) == null)break;
            field = field.getLinearNextInRectangle(from,to);
        }

        return animalsHashMap.get(field) == null ? Optional.empty() : Optional.of(field);
    }

    public Vector2d getRandomNeighbourField(Vector2d field) {
        Vector2d from = field.subtract(new Vector2d(1,1));
        Vector2d to = field.add(new Vector2d(2,2));
        Vector2d neighbourField = Vector2d.getRandomVector(from,to);
        if(neighbourField.equals(field))neighbourField = neighbourField.getLinearNextInRectangle(from,to);
        return neighbourField;
    }

    public void drawMap(Canvas canvas) {
        double xScale = canvas.getWidth() / (double) mapDimensions.x;
        double yScale = canvas.getHeight() / (double) mapDimensions.y;

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        graphicsContext.setFill(Color.DARKGREEN);
        graphicsContext.fillRect(jungleLowerLeftCorner.x * xScale, jungleLowerLeftCorner.y * yScale, jungleDimensions.x * xScale, jungleDimensions.y * yScale);

        animalsList.forEach(animal -> animal.draw(graphicsContext, xScale, yScale));
        plantsInsideJungleList.forEach(plant -> plant.draw(graphicsContext, xScale, yScale));
        plantsOutsideJungleList.forEach(plant -> plant.draw(graphicsContext, xScale, yScale));

    }


    public void addAnimal(Animal animal) {
        animalsList.add(animal);

    }

    public void addPlantInsideJungle(int energyFromEatingPlant) {
        Optional<Vector2d> optionalNewPlantPosition = getPlantlessJungleField();
        if(optionalNewPlantPosition.isEmpty()) return;

        Vector2d newPlantPosition = optionalNewPlantPosition.get();
        plantsInsideJungleList.add(new Plant(newPlantPosition, energyFromEatingPlant));


    }

    private Optional<Vector2d> getPlantlessJungleField() {
        if (plantsInsideJungleList.size() >= jungleDimensions.toAreaOfRectangle()) return Optional.empty();

        LinkedList<Vector2d> plantsPositions = plantsInsideJungleList.stream().map(Plant::getPosition).collect(Collectors.toCollection(LinkedList::new));
        Vector2d jungleField = Vector2d.getRandomVector(jungleLowerLeftCorner, jungleUpperRightCorner);
        while (plantsPositions.contains(jungleField))
            jungleField = jungleField.getLinearNextInRectangle(jungleLowerLeftCorner, jungleUpperRightCorner);
        return Optional.of(jungleField);

    }



    public void addPlantOutsideJungle(int energyFromEatingPlant) {
        Optional<Vector2d> optionalNewPlantPosition = getPlantlessJungleField();
        //if(optionalNewPlantPosition.isEmpty()) return;


    }

    private Optional<Vector2d> getPlantlessSteppeField() {
        if (plantsOutsideJungleList.size() == mapDimensions.toAreaOfRectangle() - jungleDimensions.toAreaOfRectangle())
            return Optional.empty();


        LinkedList<Vector2d> plantsPositions = plantsOutsideJungleList.stream().map(Plant::getPosition).collect(Collectors.toCollection(LinkedList::new));
        Vector2d steppeField = Vector2d.getRandomVector(new Vector2d(0,0), mapDimensions);

        do{
            steppeField = steppeField.getLinearNextInRectangle(jungleLowerLeftCorner, jungleUpperRightCorner);
        }while (plantsPositions.contains(steppeField));

        return Optional.of(steppeField);

    }


    public void moveAnimals() {
        animalsList.forEach(Animal::move);
        animalsHashMap = new HashMap<>();
        for (Animal animal : animalsList) {
            if (!animalsHashMap.containsKey(animal.getPosition()))
                animalsHashMap.put(animal.getPosition(), new LinkedList<>());
            animalsHashMap.get(animal.getPosition()).add(animal);
        }
    }

    public void eatPlants() {

        eatPlantsIn(plantsInsideJungleList);
        eatPlantsIn(plantsOutsideJungleList);

    }

    private void eatPlantsIn(LinkedList<Plant> plantsList) {
        ListIterator<Plant> iterator = plantsList.listIterator();
        while (iterator.hasNext()) {
            Plant plant = iterator.next();
            if (animalsHashMap.get(plant.position) == null) continue;

            Animal animal = Collections.max(animalsHashMap.get(plant.position), Comparator.comparingInt(Animal::getEnergy));
            animal.eat(plant.energy);
            iterator.remove();
        }
    }

    public void reproduceAnimals(int minEnergyForReproduction) {
        for (LinkedList<Animal> animalsOnField : animalsHashMap.values()) {
            Optional<Animal> newAnimalOptional = animalsOnField.stream()
                    .filter(animal -> animal.getEnergy() >= minEnergyForReproduction)
                    .sorted(Comparator.comparingInt(Animal::getEnergy).reversed())
                    .limit(2)
                    .reduce(Animal::reproduce);

            if (newAnimalOptional.isEmpty()) continue;

            animalsList.add(newAnimalOptional.get());

        }

    }

    public void removeDeadAnimals() {
        //TODO:Should I prioritise making animalList final
        animalsList = animalsList.stream().filter(Animal::isAlive).collect(Collectors.toCollection(LinkedList::new));
    }

    private boolean isInJungle(Vector2d field){
        return field.isInsideRectangle(jungleLowerLeftCorner,jungleUpperRightCorner);
    }
}
