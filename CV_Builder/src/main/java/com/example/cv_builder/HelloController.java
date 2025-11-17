package com.example.cv_builder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class HelloController {

    // This method will be called when the "Create New CV" button is clicked
    public void handleCreateCV() {
        try {
            // Load the FXML file for the form screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml")); // Correct path
            Parent root = loader.load();

            // Show the form screen
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Create New CV");

            // Center the window on the screen
            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
