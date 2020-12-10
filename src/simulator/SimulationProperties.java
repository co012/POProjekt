package simulator;

public final class SimulationProperties {

    public final int mapWidth;
    public final int mapHeight;
    public final int animalStartEnergy;
    public final int animalMoveEnergy;
    public final int energyFromPlant;
    public final int jungleWidth;
    public final int jungleHeight;
    public final int startAnimalsNumber;
    public final int plantsPerDayInsideJungle;
    public final int plantsPerDayOutsideJungle;
    public final int minEnergyForReproduction;

    @Override
    public String toString() {
        return "SimulationProperties{" +
                "mapWidth=" + mapWidth +
                ", mapHeight=" + mapHeight +
                ", animalStartEnergy=" + animalStartEnergy +
                ", animalMoveEnergy=" + animalMoveEnergy +
                ", energyFromPlant=" + energyFromPlant +
                ", jungleWidth=" + jungleWidth +
                ", jungleHeight=" + jungleHeight +
                ", startAnimalsNumber=" + startAnimalsNumber +
                ", plantsPerDayInsideJungle=" + plantsPerDayInsideJungle +
                ", plantsPerDayOutsideJungle=" + plantsPerDayOutsideJungle +
                ", minEnergyForReproduction=" + minEnergyForReproduction +
                '}';
    }

    public SimulationProperties(int mapWidth, int mapHeight, int animalStartEnergy, int animalMoveEnergy, int energyFromPlant, int jungleWidth, int jungleHeight, int startAnimalsNumber, int plantsPerDayInsideJungle, int plantsPerDayOutsideJungle, int minEnergyForReproduction) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.animalStartEnergy = animalStartEnergy;
        this.animalMoveEnergy = animalMoveEnergy;
        this.energyFromPlant = energyFromPlant;
        this.jungleHeight = jungleHeight;
        this.jungleWidth = jungleWidth;
        this.startAnimalsNumber = startAnimalsNumber;
        this.plantsPerDayInsideJungle = plantsPerDayInsideJungle;
        this.plantsPerDayOutsideJungle = plantsPerDayOutsideJungle;
        this.minEnergyForReproduction = minEnergyForReproduction;
    }

    public boolean areValid() {
        return jungleWidth <= mapWidth && jungleHeight <= mapHeight;
    }
}
