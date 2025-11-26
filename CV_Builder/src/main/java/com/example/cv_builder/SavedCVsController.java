package com.example.cv_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SavedCVsController {

    @FXML
    private TableView<DatabaseManager.CVData> cvsTable;
    @FXML
    private TableColumn<DatabaseManager.CVData, Integer> idColumn;
    @FXML
    private TableColumn<DatabaseManager.CVData, String> nameColumn;
    @FXML
    private TableColumn<DatabaseManager.CVData, String> emailColumn;
    @FXML
    private TableColumn<DatabaseManager.CVData, String> phoneColumn;
    @FXML
    private TableColumn<DatabaseManager.CVData, String> updatedColumn;

    private ObservableList<DatabaseManager.CVData> cvDataList;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        updatedColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        loadCVs();
    }

    private void loadCVs() {
        javafx.concurrent.Task<List<DatabaseManager.CVData>> task = new javafx.concurrent.Task<>() {
            @Override
            protected List<DatabaseManager.CVData> call() throws Exception {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                return dbManager.getAllCVs();
            }
        };

        task.setOnSucceeded(e -> {
            cvDataList = FXCollections.observableArrayList(task.getValue());
            cvsTable.setItems(cvDataList);
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            showAlert("Error", "Failed to load CVs: " + exception.getMessage(), Alert.AlertType.ERROR);
        });

        new Thread(task).start();
    }

    @FXML
    private void handleViewCV() {
        DatabaseManager.CVData selectedCV = cvsTable.getSelectionModel().getSelectedItem();
        if (selectedCV == null) {
            showAlert("No Selection", "Please select a CV to view.", Alert.AlertType.WARNING);
            return;
        }

        DatabaseManager dbManager = DatabaseManager.getInstance();
        CVModel cvModel = dbManager.getCVById(selectedCV.getId());

        if (cvModel != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_preview.fxml"));
                Parent root = loader.load();

                CVPreviewController previewController = loader.getController();
                previewController.setCVData(cvModel, cvModel.getProfilePhoto());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("CV Preview - " + cvModel.getFullName());
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load CV preview: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleEditCV() {
        DatabaseManager.CVData selectedCV = cvsTable.getSelectionModel().getSelectedItem();
        if (selectedCV == null) {
            showAlert("No Selection", "Please select a CV to edit.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
            Parent root = loader.load();

            FormController formController = loader.getController();
            formController.setEditMode(selectedCV.getId());

            Stage stage = new Stage();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.setTitle("Edit CV - " + selectedCV.getFullName());
            stage.setMaximized(true);
            stage.show();

            stage.setOnHidden(e -> loadCVs());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load edit form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCV() {
        DatabaseManager.CVData selectedCV = cvsTable.getSelectionModel().getSelectedItem();
        if (selectedCV == null) {
            showAlert("No Selection", "Please select a CV to delete.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete CV");
        confirmAlert.setContentText("Are you sure you want to delete the CV for " + selectedCV.getFullName() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            javafx.concurrent.Task<Boolean> task = new javafx.concurrent.Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    return dbManager.deleteCV(selectedCV.getId());
                }
            };

            task.setOnSucceeded(e -> {
                if (task.getValue()) {
                    showAlert("Success", "CV deleted successfully!", Alert.AlertType.INFORMATION);
                    loadCVs();
                } else {
                    showAlert("Error", "Failed to delete CV.", Alert.AlertType.ERROR);
                }
            });

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
                showAlert("Error", "Failed to delete CV: " + exception.getMessage(), Alert.AlertType.ERROR);
            });

            new Thread(task).start();
        }
    }

    @FXML
    private void handleRefresh() {
        loadCVs();
    }

    @FXML
    private void handleBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cvsTable.getScene().getWindow();
            Scene scene = new Scene(root, 320, 240);
            stage.setScene(scene);
            stage.setTitle("CV Builder");
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Home screen: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}