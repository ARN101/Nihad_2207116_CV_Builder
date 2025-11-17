package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CVPreviewController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label educationLabel;
    @FXML private Label skillsLabel;
    @FXML private Label workExperienceLabel;
    @FXML private Label projectsLabel;

    // Set the CV data on the preview screen
    public void setCV(String name, String email, String phone, String address, String education, String skills, String workExperience, String projects) {
        nameLabel.setText("Name: " + name);
        emailLabel.setText("Email: " + email);
        phoneLabel.setText("Phone: " + phone);
        addressLabel.setText("Address: " + address);
        educationLabel.setText("Education: " + education);
        skillsLabel.setText("Skills: " + skills);
        workExperienceLabel.setText("Work Experience: " + workExperience);
        projectsLabel.setText("Projects: " + projects);
    }
}
