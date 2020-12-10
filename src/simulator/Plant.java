package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plant implements IDrawableMapElement {
    public final int energy;
    public final Vector2d position;
    private final static String PLANT_COLOR = "#119999";

    public Plant(Vector2d position, int energy) {
        this.energy = energy;
        this.position = position;
    }

    @Override
    public void draw(GraphicsContext graphicsContext, double xScale, double yScale) {
        graphicsContext.setFill(Color.web(PLANT_COLOR));
        double x = position.x * xScale;
        double y = position.y * yScale;
        graphicsContext.fillRect(x,y,xScale,yScale);
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }
}
