package simulator.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simulator.Vector2d;

public class Plant implements IDrawableWorldMapElement {
    private final int nutritionalValue;
    private final Vector2d position;
    private final static String PLANT_COLOR = "#119999";

    public Plant(Vector2d position, int nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
        this.position = position;
    }

    @Override
    public void draw(GraphicsContext graphicsContext, double xScale, double yScale) {
        graphicsContext.setFill(Color.web(PLANT_COLOR));
        double x = position.x * xScale;
        double y = position.y * yScale;
        graphicsContext.fillRect(x, y, xScale, yScale);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getNutritionalValue() {
        return nutritionalValue;
    }
}
