package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

    // This method will be called when the "Generate CV" button is clicked
    @FXML
    private void handleGenerateCV() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String education = educationField.getText();
        String skills = skillsField.getText();

        // Display the entered data in an alert (for now)
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("CV Generated");
        alert.setHeaderText("Here is your generated CV:");
        alert.setContentText("Name: " + fullName + "\nEmail: " + email + "\nPhone: " + phone +
                "\nEducation: " + education + "\nSkills: " + skills);
        alert.showAndWait();
    }
}
