package com.example.cv_builder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class HelloController {

    public void handleCreateCV() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.setTitle("Create New CV");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleViewSavedCVs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("saved_cvs.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.setTitle("Saved CVs");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHover(javafx.scene.input.MouseEvent event) {
        // Simple hover effect or just a placeholder to prevent crash
        if (event.getSource() instanceof javafx.scene.control.Button) {
            javafx.scene.control.Button button = (javafx.scene.control.Button) event.getSource();
            // We could add a scale effect here, but for now just ensuring the method exists is enough to fix the crash.
            // Let's add a subtle scale up
            button.setScaleX(1.05);
            button.setScaleY(1.05);
            
            // We should also reset it on exit, but the FXML only has onMouseEntered.
            // To make it complete, we'd need to add onMouseExited to the FXML.
            // For now, let's just make it print to console to verify it works, 
            // and maybe add a reset on exit if I edit the FXML.
            // Given the user wants it "ready to run", I'll just add the method to satisfy the loader.
            // To be safe and not leave the button stuck in scaled state without an exit handler, 
            // I will NOT apply a permanent state change that requires an exit handler unless I add the exit handler.
            // So I will just leave it empty or print a log.
            // Wait, the user might appreciate a working effect. 
            // I'll check hello-view.fxml again. It only has onMouseEntered.
            // So I will just add the method.
        }
    }

    public void handleExit(javafx.scene.input.MouseEvent event) {
        if (event.getSource() instanceof javafx.scene.control.Button) {
            javafx.scene.control.Button button = (javafx.scene.control.Button) event.getSource();
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        }
    }
}