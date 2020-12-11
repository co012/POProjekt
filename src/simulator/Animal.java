package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class Animal implements IDrawableMapElement {

    private Vector2d position;
    private final int startEnergy;
    private final int moveEnergy;
    private int energy;
    private int age;
    private MapDirection mapDirection;
    private final WorldMap map;
    private final Genotype genotype;
    private final static String ANIMAL_COLOR = "#A0522D";




    public Animal(WorldMap map, Vector2d position, int startEnergy, int moveEnergy) {
        this(map, position, startEnergy, moveEnergy, new Genotype());
    }


    public Animal(WorldMap map, Vector2d position, int startEnergy, int moveEnergy, Genotype genotype) {
        if(position == null) throw new  IllegalArgumentException("position can't be null");
        this.position = position;
        this.map = map;
        this.energy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.genotype = genotype;
        this.mapDirection = MapDirection.getRandomDirection();
        this.startEnergy = startEnergy;
        age = 0;
    }


    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void move() {
        int shift = genotype.getRandomGen();
        mapDirection = mapDirection.shiftMapDirection(shift);
        position = map.getNewPosition(position, mapDirection);
        energy -= moveEnergy;

    }

    public void beginDay(){
        age++;
    }

    public Animal reproduce(Animal that) {
        Genotype childGenotype = new Genotype(this.genotype, that.genotype);
        Vector2d childPosition = map.getRandomNeighbourEmptyFieldIfPossible(position);
        int childEnergy = (that.energy + that.energy) / 4;
        this.energy = (int) (this.energy * 0.75);
        that.energy = (int) (that.energy * 0.75);
        return new Animal(map, childPosition, childEnergy, moveEnergy, childGenotype);
    }

    public void eat(int energyFromFood){
        this.energy+=energyFromFood;
    }


    @Override
    public void draw(GraphicsContext graphicsContext, double xScale, double yScale) {
        double alpha = energy / (double)startEnergy * 0.5;
        alpha = Math.min(alpha,1);
        alpha = Math.max(alpha,.1);

        graphicsContext.setFill(Color.web(ANIMAL_COLOR,alpha));

        double x = position.x * xScale;
        double y = position.y * yScale;
        graphicsContext.fillRect(x,y,xScale,yScale);
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public int getEnergy() {
        return energy;
    }
    public int getAge() {
        return age;
    }

    public int[] getGeneFrequency(){
        return genotype.getGeneFrequencyArray();
    }
}
