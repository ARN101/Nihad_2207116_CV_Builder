package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FormController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private TextArea educationField;
    @FXML private TextArea skillsField;
    @FXML private TextArea workExperienceField;
    @FXML private TextArea projectsField;
    @FXML private Button uploadPhotoButton;
    @FXML private ImageView photoPreview;

    private Image selectedPhoto;

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                selectedPhoto = new Image(selectedFile.toURI().toString());
                photoPreview.setImage(selectedPhoto);
                photoPreview.setVisible(true);
            } catch (Exception e) {
                showAlert("Error loading image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGenerateCV() {
        StringBuilder errors = new StringBuilder();

        if (fullNameField.getText().trim().isEmpty()) {
            errors.append("• Full Name is required\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errors.append("• Email is required\n");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errors.append("• Email format is invalid\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errors.append("• Phone Number is required\n");
        }

        if (errors.length() > 0) {
            showAlert("Please fix the following errors:\n\n" + errors.toString());
        } else {
            CVModel cvData = new CVModel(
                    fullNameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim(),
                    educationField.getText().trim(),
                    skillsField.getText().trim(),
                    workExperienceField.getText().trim(),
                    projectsField.getText().trim()
            );

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_preview.fxml"));
                Parent root = loader.load();

                CVPreviewController previewController = loader.getController();
                previewController.setCVData(cvData, selectedPhoto);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("CV Preview");
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading CV preview: " + e.getMessage());
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Missing Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}