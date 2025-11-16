package com.example.cv_builder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the form screen
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);  // Adjust size as needed
        stage.setTitle("CV Builder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
