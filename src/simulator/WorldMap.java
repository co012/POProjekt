package simulator;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldMap {

    private final Vector2d mapDimensions;
    private final Vector2d jungleLowerLeftCorner;
    private final Vector2d jungleUpperRightCorner;
    private final Vector2d jungleDimensions;
    private final List<Animal> animalsList;
    private final List<Plant> plantsList;
    private final List<Vector2d> plantlessJungleFieldList;
    private final List<Vector2d> plantlessSteppeFieldList;
    private HashMap<Vector2d, LinkedList<Animal>> animalsHashMap;

    private int todayChildrenNumber;
    private double deadAnimalsNumber;
    private double sumOfDeadAnimalsAge;


    public WorldMap(Vector2d mapDimensions, Vector2d jungleLowerLeftCorner, Vector2d jungleUpperRightCorner) {
        this.mapDimensions = mapDimensions;
        this.jungleLowerLeftCorner = jungleLowerLeftCorner;
        this.jungleUpperRightCorner = jungleUpperRightCorner;
        this.jungleDimensions = jungleUpperRightCorner.subtract(jungleLowerLeftCorner);
        animalsList = Collections.synchronizedList( new LinkedList<>());
        plantsList = Collections.synchronizedList( new LinkedList<>());
        animalsHashMap = new HashMap<>();

        plantlessJungleFieldList = new LinkedList<>();
        plantlessSteppeFieldList = new LinkedList<>();

        for (int i = 0; i < mapDimensions.x; i++) {
            for (int j = 0; j < mapDimensions.y; j++) {
                Vector2d mapField = new Vector2d(i, j);

                if (isInJungle(mapField)) {
                    plantlessJungleFieldList.add(mapField);
                } else {
                    plantlessSteppeFieldList.add(mapField);
                }
            }
        }

        Collections.shuffle(plantlessJungleFieldList);
        Collections.shuffle(plantlessSteppeFieldList);

        todayChildrenNumber = 0;

    }

    public int getAnimalsNumber() {
        return animalsList.size();
    }

    public int getPlantsNumber() {
        return plantsList.size();
    }


    public Vector2d getNewPosition(Vector2d oldPosition, MapDirection mapDirection) {
        Vector2d v = oldPosition.add(mapDirection.mapDirectionToVector2d());
        return v.moduloPositive(mapDimensions);
    }

    //TODO: think about better name
    public Vector2d getRandomNeighbourEmptyFieldIfPossible(Vector2d vector) {
        Vector2d from = vector.subtract(new Vector2d(1, 1));
        Vector2d to = vector.add(new Vector2d(2, 2));
        Vector2d field = Vector2d.getRandomVector(from, to);
        for (int i = 0; i < 9; i++) {
            if (animalsHashMap.get(field) == null) break;
            field = field.getLinearNextInRectangle(from, to);
        }

        if (field.equals(vector)) field.getLinearNextInRectangle(from, to);

        return field;
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
        plantsList.forEach(plant -> plant.draw(graphicsContext, xScale, yScale));

    }

    public void spawnAnimals(int animalsNumber, int startEnergy, int moveEnergy) {
        for (int i = 0; i < animalsNumber; i++) {
            spawnAnimal(startEnergy, moveEnergy);
        }
    }

    private void spawnAnimal(int startEnergy, int moveEnergy) {
        Vector2d positionCandidate = Vector2d.getRandomVector(Vector2d.ZERO, mapDimensions);

        for (int i = 0; i < mapDimensions.toAreaOfRectangle(); i++) {
            if (!animalsHashMap.containsKey(positionCandidate)) break;
            positionCandidate = positionCandidate.getLinearNextInRectangle(Vector2d.ZERO, mapDimensions);
        }

        addAnimal(new Animal(this, positionCandidate, startEnergy, moveEnergy));

    }

    private void addAnimal(Animal animal) {
        animalsList.add(animal);
        LinkedList<Animal> animalsOnField = animalsHashMap.get(animal.getPosition());

        if (animalsOnField == null) {
            LinkedList<Animal> newAnimalsOnFieldList = new LinkedList<>();
            newAnimalsOnFieldList.add(animal);
            animalsHashMap.put(animal.getPosition(), newAnimalsOnFieldList);
        } else {
            animalsOnField.add(animal);
        }

    }

    public void spawnPlantsInsideJungle(int plantsNumber, int energyFromEatingPlant) {

        for (int i = 0; i < plantsNumber; i++) {
            spawnPlant(new LinkedList<>(plantlessJungleFieldList), energyFromEatingPlant);
        }

    }

    public void spawnPlantsOutsideJungle(int plantsNumber, int energyFromEatingPlant) {

        for (int i = 0; i < plantsNumber; i++) {
            spawnPlant(new LinkedList<>(plantlessSteppeFieldList), energyFromEatingPlant);
        }

    }

    private void spawnPlant(LinkedList<Vector2d> positionCandidates, int energyFromEatingPlant) {

        Vector2d position = null;
        for (Vector2d positionCandidate : positionCandidates) {
            if (animalsHashMap.containsKey(positionCandidate)) continue;

            position = positionCandidate;
            break;
        }

        if (position == null) return;

        plantsList.add(new Plant(position, energyFromEatingPlant));

        if (isInJungle(position)) {
            plantlessJungleFieldList.remove(position);
        } else {
            plantlessSteppeFieldList.remove(position);
        }


    }

    public void beginDay(){
        todayChildrenNumber = 0;
        animalsList.forEach(Animal::beginDay);
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
        LinkedList<Plant> copyOfPlantsList = new LinkedList<>(plantsList);

        for (Plant plant : copyOfPlantsList) {
            if (!animalsHashMap.containsKey(plant.getPosition())) continue;

            LinkedList<Animal> animalsOnField = animalsHashMap.get(plant.getPosition());
            LinkedList<Animal> animalsWithGreatestEnergy = getAnimalsWithGreatestEnergy(animalsOnField);
            int energyPerAnimal = plant.getNutritionalValue() / animalsWithGreatestEnergy.size();
            for (Animal animal : animalsWithGreatestEnergy) {
                animal.eat(energyPerAnimal);
            }

            if (isInJungle(plant.getPosition())) {
                plantlessJungleFieldList.add(plant.getPosition());
            } else {
                plantlessSteppeFieldList.add(plant.getPosition());
            }

            plantsList.remove(plant);
        }
    }

    private LinkedList<Animal> getAnimalsWithGreatestEnergy(LinkedList<Animal> animals) {
        Optional<Animal> optionalOfAnimalWithGreatestEnergy = animals.stream().max(Comparator.comparingInt(Animal::getEnergy));
        if (optionalOfAnimalWithGreatestEnergy.isEmpty()) {
            throw new IllegalArgumentException("animals can't be an empty list");
        }

        Animal animalWithGreatestEnergy = optionalOfAnimalWithGreatestEnergy.get();
        return animals.stream()
                .filter(animal -> animal.getEnergy() == animalWithGreatestEnergy.getEnergy())
                .collect(Collectors.toCollection(LinkedList::new));

    }


    public void reproduceAnimals(int minEnergyForReproduction) {
        HashMap<Vector2d, LinkedList<Animal>> copyOfAnimalsHashMap = new HashMap<>(animalsHashMap);
        for (LinkedList<Animal> animalsOnField : copyOfAnimalsHashMap.values()) {
            LinkedList<Animal> parents = animalsOnField.stream()
                    .filter(animal -> animal.getEnergy() >= minEnergyForReproduction)
                    .sorted(Comparator.comparingInt(Animal::getEnergy).reversed())
                    .limit(2)
                    .collect(Collectors.toCollection(LinkedList::new));

            if (parents.size() < 2) continue;

            addAnimal(parents.get(0).reproduce(parents.get(1)));
            todayChildrenNumber++;
        }

    }

    public void removeDeadAnimals() {
        LinkedList<Animal> deadAnimalsList = animalsList.stream()
                .filter(Animal::isDead)
                .collect(Collectors.toCollection(LinkedList::new));

        if (deadAnimalsList.isEmpty()) return;

        deadAnimalsList.forEach(Animal::onDie);

        double ageSum = deadAnimalsList
                .stream()
                .mapToDouble(Animal::getAge)
                .sum();

        deadAnimalsNumber += deadAnimalsList.size();
        sumOfDeadAnimalsAge += ageSum;

        animalsList.removeAll(deadAnimalsList);
    }

    private boolean isInJungle(Vector2d field) {
        return field.isInsideRectangle(jungleLowerLeftCorner, jungleUpperRightCorner);
    }

    public double getAvgEnergy() {
        int energySum = animalsList
                .stream()
                .mapToInt(Animal::getEnergy)
                .sum();
        return energySum / (double)animalsList.size();
    }

    public double getLifeExpectancy() {
        return deadAnimalsNumber == 0 ? 0 : sumOfDeadAnimalsAge/deadAnimalsNumber;
    }

    public double getAvgChildrenNumber() {
        return todayChildrenNumber / (double) animalsList.size();
    }

    public Optional<Genotype> getMostPopularGenotype(){



        Optional<Map.Entry<Genotype,Long>> genotypeAndFrequency = animalsList.stream()
                .map(Animal::getGenotype)
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if(genotypeAndFrequency.isEmpty()) return  Optional.empty();
        else return Optional.of(genotypeAndFrequency.get().getKey());
    }

    public LinkedList<Animal> getAnimalsOnField(Vector2d field){
        return animalsHashMap.containsKey(field) ? animalsHashMap.get(field) : new LinkedList<>();
    }

    public void selectAnimalsWithMostPopularGenotype(){
        Optional<Genotype> genotypeOptional = getMostPopularGenotype();
        if(genotypeOptional.isEmpty())return;
        Genotype mostPopularGenotype = genotypeOptional.get();
        animalsList.stream()
                .filter(animal -> animal.getGenotype().equals(mostPopularGenotype))
                .forEach(animal -> animal.selectAnimal(Animal.SELECTED_MOST_POPULAR_ANIMAL_COLOR));
    }

    public void unSelectAllAnimals(){
        animalsList.forEach(Animal::unSelectAnimal);
    }
}
