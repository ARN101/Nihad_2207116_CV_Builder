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
        if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert("Please fill in all the required fields.");
        } else {
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String education = educationField.getText();
            String skills = skillsField.getText();
            String workExperience = workExperienceField.getText();
            String projects = projectsField.getText();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_preview.fxml"));
                Parent root = loader.load();

                CVPreviewController previewController = loader.getController();
                previewController.setCV(fullName, email, phone, address, education, skills, workExperience, projects, selectedPhoto);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("CV Preview");
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Missing Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}