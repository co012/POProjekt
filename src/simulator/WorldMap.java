package simulator;


import javafx.scene.canvas.Canvas;

public class WorldMap {

    private final Vector2d dimensions;

    public WorldMap(int mapWidth, int mapHeight){
        this(new Vector2d(mapWidth,mapHeight));
    }
    public WorldMap(Vector2d dimensions){
        this.dimensions = dimensions;
    }


    public Vector2d getNewPosition(Vector2d oldPosition,MapDirection mapDirection){
        Vector2d v = oldPosition.add(mapDirection.mapDirectionToVector2d());
        return v.modulo(dimensions);
    }


}
