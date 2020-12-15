package simulator.map;

import simulator.Vector2d;

import java.util.Random;

public enum MapDirection {
    NORTH(0),
    NORTH_EAST(1),
    EAST(2),
    SOUTH_EAST(3),
    SOUTH(4),
    SOUTH_WEST(5),
    WEST(6),
    NORTH_WEST(7);

    private final int val;

    MapDirection(int val) {
        this.val = val;
    }

    public MapDirection shiftMapDirection(int shift) {
        int n = MapDirection.values().length;
        int i = (this.val + shift) % n;
        return MapDirection.values()[i];

    }

    public static MapDirection getRandomDirection() {
        int n = MapDirection.values().length;
        int i = new Random().nextInt(n);
        return MapDirection.values()[i];
    }

    public Vector2d mapDirectionToVector2d() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }
}
