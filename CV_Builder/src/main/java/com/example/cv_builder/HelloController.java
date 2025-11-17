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
            stage.setScene(new Scene(root));
            stage.setTitle("Create New CV");


            stage.setFullScreen(true);


            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
