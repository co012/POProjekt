package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.ref.WeakReference;
import java.util.*;

public class Animal implements IDrawableWorldMapElement {

    private Vector2d position;
    private final int startEnergy;
    private final int moveEnergy;
    private int energy;
    private int age;
    private MapDirection mapDirection;
    private final WorldMap map;
    private final Genotype genotype;
    private final static String ANIMAL_COLOR = "#A0522D";
    public final static String SELECTED_MOST_POPULAR_ANIMAL_COLOR = "#FFFFFF";
    public final static String FOLLOWED_ANIMAL_COLOR = "#10109F";
    private String color;

    private final LinkedList<WeakReference<FollowedAnimalStatisticsObserver>> lifeObservers;
    private final LinkedList<WeakReference<FollowedAnimalStatisticsObserver>> descendantsObservers;





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
        color = ANIMAL_COLOR;

        lifeObservers = new LinkedList<>();
        descendantsObservers = new LinkedList<>();
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
        Animal child = new Animal(map, childPosition, childEnergy, moveEnergy, childGenotype);

        onChildBorn(child);

        return child;
    }

    private void onChildBorn(Animal child) {
        executeOnWeakReferenceList(lifeObservers, followedAnimalStatisticsObserver -> {
            child.registerDescendantsObserver(followedAnimalStatisticsObserver);
            followedAnimalStatisticsObserver.onChildBorn();
        });

        executeOnWeakReferenceList(descendantsObservers,followedAnimalStatisticsObserver -> {
            child.registerDescendantsObserver(followedAnimalStatisticsObserver);
            followedAnimalStatisticsObserver.onDescendantBorn();
        });

    }

    public void eat(int energyFromFood){
        this.energy+=energyFromFood;
    }


    @Override
    public void draw(GraphicsContext graphicsContext, double xScale, double yScale) {
        double alpha = energy / (double)startEnergy * 0.5;
        alpha = Math.min(alpha,1);
        alpha = Math.max(alpha,.1);

        graphicsContext.setFill(Color.web(color,alpha));

        double x = position.x * xScale;
        double y = position.y * yScale;
        graphicsContext.fillRect(x,y,xScale,yScale);
    }

    public boolean isDead(){
        return energy <= 0;
    }

    public void registerFollowAnimalObserver(FollowedAnimalStatisticsObserver followedAnimalStatisticsObserver){
        lifeObservers.add(new WeakReference<>(followedAnimalStatisticsObserver));
        registerDescendantsObserver(followedAnimalStatisticsObserver);
    }


    private void registerDescendantsObserver(FollowedAnimalStatisticsObserver followedAnimalStatisticsObserver){
        descendantsObservers.add(new WeakReference<>(followedAnimalStatisticsObserver));
    }



    public void onDie(){
        executeOnWeakReferenceList(lifeObservers,FollowedAnimalStatisticsObserver::onFollowedAnimalDead);
    }

    private void executeOnWeakReferenceList(LinkedList<WeakReference<FollowedAnimalStatisticsObserver>> weakReferences, IFollowedAnimalStatisticsObserverRunnable runnable){
        ListIterator<WeakReference<FollowedAnimalStatisticsObserver>> weakReferenceListIterator = weakReferences.listIterator();
        while (weakReferenceListIterator.hasNext()){
            WeakReference<FollowedAnimalStatisticsObserver> followedAnimalStatisticsObserverWeakReference = weakReferenceListIterator.next();
            FollowedAnimalStatisticsObserver followedAnimalStatisticsObserver = followedAnimalStatisticsObserverWeakReference.get();
            if(followedAnimalStatisticsObserver == null){
                weakReferenceListIterator.remove();
                continue;
            }
            runnable.executeOn(followedAnimalStatisticsObserver);
        }
    }
    private void removeNullReferencesFromList(LinkedList<WeakReference<FollowedAnimalStatisticsObserver>> list){
        executeOnWeakReferenceList(list,(e) -> {});
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

    public Genotype getGenotype(){
        return genotype;
    }

    public void unSelectAnimal(){
        removeNullReferencesFromList(lifeObservers);
        color = lifeObservers.isEmpty() ? ANIMAL_COLOR: FOLLOWED_ANIMAL_COLOR;
    }

    public void selectAnimal(String selectionColor){
        color = selectionColor;
    }
}
