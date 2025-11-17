package com.example.cv_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CVPreviewController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label educationLabel;
    @FXML private Label skillsLabel;
    @FXML private Label workExperienceLabel;
    @FXML private Label projectsLabel;
    @FXML private ImageView profilePhoto;

    public void setCV(String name, String email, String phone, String address, String education, String skills, String workExperience, String projects, Image photo) {
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        addressLabel.setText(address);
        educationLabel.setText(education);
        skillsLabel.setText(skills);
        workExperienceLabel.setText(workExperience);
        projectsLabel.setText(projects);

        if (photo != null) {
            profilePhoto.setImage(photo);
            profilePhoto.setVisible(true);
        } else {
            profilePhoto.setVisible(false);
        }
    }
}