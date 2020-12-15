package simulator.right_sidebar;

import simulator.map.Animal;

import java.util.HashMap;
import java.util.Optional;

public class FollowedAnimalStatisticsObserver {
    private final HashMap<Integer, Integer> numberOfChildrenToDay;
    private final HashMap<Integer, Integer> numberOfDescendantsToDay;
    private int numberOfChildren;
    private int numberOfDescendants;
    private Integer dayOfDeath;
    private boolean isFollowedAnimalDead;

    public FollowedAnimalStatisticsObserver(Animal followedAnimal) {
        numberOfChildrenToDay = new HashMap<>();
        numberOfDescendantsToDay = new HashMap<>();
        numberOfChildren = 0;
        numberOfDescendants = 0;
        followedAnimal.registerFollowAnimalObserver(this);
        isFollowedAnimalDead = false;

    }

    public boolean containsDataForDay(int n) {
        return numberOfDescendantsToDay.containsKey(n);
    }

    public Optional<Integer> getDayOfDead() {
        return Optional.ofNullable(dayOfDeath);
    }

    public int getNumberOfChildrenToDay(int n) {
        return numberOfChildrenToDay.get(n);
    }

    public int getNumberOfDescendantsToDay(int n) {
        return numberOfDescendantsToDay.get(n);
    }

    public void collectDataForDay(int day) {
        numberOfDescendantsToDay.put(day, numberOfDescendants);
        numberOfChildrenToDay.put(day, numberOfChildren);
        if (isFollowedAnimalDead && dayOfDeath == null) {
            dayOfDeath = day;
        }
    }

    public void onChildBorn() {
        numberOfChildren++;
    }

    public void onDescendantBorn() {
        numberOfDescendants++;
    }

    public void onFollowedAnimalDead() {
        isFollowedAnimalDead = true;
    }

}
