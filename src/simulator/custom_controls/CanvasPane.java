package simulator.custom_controls;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;


public class CanvasPane extends Pane {

    public final Canvas canvas;

    public CanvasPane(Canvas canvas){
        super();
        this.canvas = canvas;
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
    }
}
