package org.museum_app.museum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.museum_app.museum.view.MuseumGUI;

import java.io.IOException;

public class MuseumApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MuseumGUI.setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(MuseumApplication.class.getResource("fxml/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 640);
        stage.setTitle("Museum App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}