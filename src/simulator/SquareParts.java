package simulator;

import java.util.LinkedList;

public enum SquareParts {
    UPPER_LEFT,
    UPPER,
    UPPER_RIGHT,
    CENTER_LEFT,
    CENTER,
    CENTER_RIGHT,
    LOWER_LEFT,
    LOWER,
    LOWER_RIGHT;
    //TODO:write a test
    public static LinkedList<SquareParts> getExistingParts(Vector2d from1, Vector2d to1, Vector2d from2, Vector2d to2){
        LinkedList<SquareParts> possibleParts = new LinkedList<>();

        if(to1.y != to2.y){
            if(from1.x != from2.x) possibleParts.add(UPPER_LEFT);
            if(to2.x != from2.x) possibleParts.add(UPPER);
            if(to1.x != to2.x) possibleParts.add(UPPER_RIGHT);
        }

        if(from2.y != to2.y){
            if(from1.x != from2.x) possibleParts.add(CENTER_LEFT);
            if(to2.x != from2.x) possibleParts.add(CENTER);
            if(to1.x != to2.x) possibleParts.add(CENTER_RIGHT);
        }

        if(from1.y != from2.y){
            if(from1.x != from2.x) possibleParts.add(LOWER_LEFT);
            if(to2.x != from2.x) possibleParts.add(LOWER);
            if(to1.x != to2.x) possibleParts.add(LOWER_RIGHT);
        }

        return possibleParts;
    }


}
