package org.example;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.MainSceneController;

public class MainScene extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainSceneController controller = new MainSceneController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Generalka");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
