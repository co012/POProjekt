package simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("start");
        FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("fxml/app.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Evolution Simulation");
        primaryStage.setScene(new Scene(root, 1080, 720));
        AppController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();

        List<String> parametersList = getParameters().getUnnamed();
        if(!parametersList.isEmpty())controller.loadFromFile(parametersList.get(0));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
