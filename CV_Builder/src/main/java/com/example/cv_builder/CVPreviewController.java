package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CVPreviewController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label educationLabel;
    @FXML private Label skillsLabel;

    public void setCV(String name, String email, String phone, String education, String skills) {
        nameLabel.setText("Name: " + name);
        emailLabel.setText("Email: " + email);
        phoneLabel.setText("Phone: " + phone);
        educationLabel.setText("Education: " + education);
        skillsLabel.setText("Skills: " + skills);
    }
}
