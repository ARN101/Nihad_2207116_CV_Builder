package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FormController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressField;
    @FXML
    private TextArea educationField;
    @FXML
    private TextArea skillsField;
    @FXML
    private TextArea workExperienceField;
    @FXML
    private TextArea projectsField;
    @FXML
    private Button uploadPhotoButton;
    @FXML
    private ImageView photoPreview;

    private Image selectedPhoto;
    private Integer editingCVId = null;

    public void setEditMode(int cvId) {
        this.editingCVId = cvId;
        loadCVData(cvId);
    }

    private void loadCVData(int cvId) {
        javafx.concurrent.Task<CVModel> task = new javafx.concurrent.Task<>() {
            @Override
            protected CVModel call() throws Exception {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                return dbManager.getCVById(cvId);
            }
        };

        task.setOnSucceeded(e -> {
            CVModel cvModel = task.getValue();
            if (cvModel != null) {
                fullNameField.setText(cvModel.getFullName());
                emailField.setText(cvModel.getEmail());
                phoneField.setText(cvModel.getPhone());
                addressField.setText(cvModel.getAddress());
                educationField.setText(cvModel.getQualifications());
                skillsField.setText(cvModel.getSkills());
                workExperienceField.setText(cvModel.getExperience());
                projectsField.setText(cvModel.getProjects());

                if (cvModel.getProfilePhoto() != null) {
                    selectedPhoto = cvModel.getProfilePhoto();
                    photoPreview.setImage(selectedPhoto);
                    photoPreview.setVisible(true);
                }
            }
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            showAlert("Error", "Failed to load CV data: " + exception.getMessage(), AlertType.ERROR);
        });

        new Thread(task).start();
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                selectedPhoto = new Image(selectedFile.toURI().toString());
                photoPreview.setImage(selectedPhoto);
                photoPreview.setVisible(true);
            } catch (Exception e) {
                showAlert("Error", "Error loading image: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleGenerateCV() {
        if (validateForm()) {
            CVModel cvData = createCVModel();

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
                showAlert("Error", "Error loading CV preview: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSaveCV() {
        if (validateForm()) {
            CVModel cvData = createCVModel();

            javafx.concurrent.Task<Boolean> task = new javafx.concurrent.Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    if (editingCVId != null) {
                        return dbManager.updateCV(editingCVId, cvData, selectedPhoto);
                    } else {
                        int cvId = dbManager.saveCV(cvData, selectedPhoto);
                        return cvId > 0;
                    }
                }
            };

            task.setOnSucceeded(e -> {
                boolean success = task.getValue();
                if (success) {
                    String message = editingCVId != null ? "CV updated successfully!" : "CV saved successfully!";
                    showAlert("Success", message, AlertType.INFORMATION);
                    clearForm();
                    editingCVId = null;
                    navigateToHome();
                } else {
                    showAlert("Error", "Failed to save CV to database.", AlertType.ERROR);
                }
            });

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
                showAlert("Error", "Failed to save CV: " + exception.getMessage(), AlertType.ERROR);
            });

            new Thread(task).start();
        }
    }

    @FXML
    private void handleSaveAndPreview() {
        if (validateForm()) {
            CVModel cvData = createCVModel();

            javafx.concurrent.Task<Integer> task = new javafx.concurrent.Task<>() {
                @Override
                protected Integer call() throws Exception {
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    if (editingCVId != null) {
                        boolean success = dbManager.updateCV(editingCVId, cvData, selectedPhoto);
                        return success ? editingCVId : -1;
                    } else {
                        return dbManager.saveCV(cvData, selectedPhoto);
                    }
                }
            };

            task.setOnSucceeded(e -> {
                int cvId = task.getValue();
                if (cvId <= 0) {
                    showAlert("Error", "Failed to save CV to database.", AlertType.ERROR);
                    return;
                }

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_preview.fxml"));
                    Parent root = loader.load();

                    CVPreviewController previewController = loader.getController();
                    previewController.setCVData(cvData, selectedPhoto);

                    // Navigate main window to home FIRST
                    clearForm();
                    editingCVId = null;
                    navigateToHome();

                    // THEN show preview window so it stays on top
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("CV Preview");
                    stage.setFullScreen(true);
                    stage.show();
                    stage.toFront(); // Ensure it's in front
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Error loading CV preview: " + ex.getMessage(), AlertType.ERROR);
                }
            });

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
                showAlert("Error", "Failed to save CV: " + exception.getMessage(), AlertType.ERROR);
            });

            new Thread(task).start();
        }
    }

    @FXML
    private void handleBackToHome() {
        navigateToHome();
    }

    private boolean validateForm() {
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
            showAlert("Validation Error", "Please fix the following errors:\n\n" + errors.toString(),
                    AlertType.WARNING);
            return false;
        }
        return true;
    }

    private CVModel createCVModel() {
        return new CVModel(
                fullNameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim(),
                educationField.getText().trim(),
                skillsField.getText().trim(),
                workExperienceField.getText().trim(),
                projectsField.getText().trim());
    }

    private void clearForm() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        educationField.clear();
        skillsField.clear();
        workExperienceField.clear();
        projectsField.clear();
        photoPreview.setVisible(false);
        selectedPhoto = null;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(root, 320, 240);
            stage.setScene(scene);
            stage.setTitle("CV Builder");
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Home screen: " + e.getMessage(), AlertType.ERROR);
        }
    }
}