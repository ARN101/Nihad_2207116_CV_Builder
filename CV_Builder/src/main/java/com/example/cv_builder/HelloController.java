package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelloController {

    @FXML
    private void handleCreateCV() {
        // Display a simple alert (for now)
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("CV Creation");
        alert.setContentText("Create New CV button clicked.");
        alert.showAndWait();
    }
}
