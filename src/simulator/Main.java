package simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/app.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Evolution Simulation");
        primaryStage.setScene(new Scene(root, 1080, 720));
        AppController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
