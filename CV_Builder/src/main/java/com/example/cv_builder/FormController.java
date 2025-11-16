package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class FormController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextArea educationField;

    @FXML
    private TextArea skillsField;

    @FXML
    private void handleGenerateCV() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String education = educationField.getText();
        String skills = skillsField.getText();

        try {
            // Load the CV preview screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_preview.fxml"));
            Parent root = loader.load();

            // Get the controller and set the CV details
            CVPreviewController previewController = loader.getController();
            previewController.setCV(fullName, email, phone, education, skills);

            // Show the preview screen
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("CV Preview");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
