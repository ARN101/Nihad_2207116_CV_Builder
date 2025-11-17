package com.example.cv_builder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class HelloController {

    // This method will be called when the "Create New CV" button is clicked
    public void handleCreateCV() {
        try {
            // Load the FXML file for the form screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
            Parent root = loader.load();

            // Show the form screen
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Create New CV");
            stage.show();
        } catch (IOException e) {
            // Show an alert instead of printing stack trace
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading form screen");
            alert.setContentText("An error occurred while loading the form. Please try again.");
            alert.showAndWait();
        }
    }
}
