package simulator;

import javafx.scene.canvas.GraphicsContext;

public interface IDrawableWorldMapElement extends IWorldMapElement {

    void draw(GraphicsContext graphicsContext, double xScale, double yScale);
}
