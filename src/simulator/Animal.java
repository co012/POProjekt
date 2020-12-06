package simulator;

import java.util.*;

public class Animal implements IWorldMapElement{

    private Vector2d position;
    private int energy;
    private MapDirection mapDirection;
    private final WorldMap map;
    private final Genotype genotype;


    public Animal(WorldMap map,Vector2d position){
        this(map,position,new Genotype());
    }


    public Animal(WorldMap map,Vector2d position, Genotype genotype){
        this.position = position;
        this.map = map;
        this.genotype = genotype;
    }


    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void move(){

    }

    public Animal reproduce(Animal that){

        return null;
    }
}
