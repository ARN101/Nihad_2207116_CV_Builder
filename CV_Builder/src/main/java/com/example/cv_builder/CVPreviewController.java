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

    public void setCVData(CVModel cvData, Image photo) {
        nameLabel.setText(cvData.getFullName());
        emailLabel.setText(cvData.getEmail());
        phoneLabel.setText(cvData.getPhone());
        addressLabel.setText(cvData.getAddress());
        educationLabel.setText(cvData.getQualifications());
        skillsLabel.setText(cvData.getSkills());
        workExperienceLabel.setText(cvData.getExperience());
        projectsLabel.setText(cvData.getProjects());

        educationLabel.setMaxWidth(Double.MAX_VALUE);
        skillsLabel.setMaxWidth(Double.MAX_VALUE);
        workExperienceLabel.setMaxWidth(Double.MAX_VALUE);
        projectsLabel.setMaxWidth(Double.MAX_VALUE);

        if (photo != null) {
            profilePhoto.setImage(photo);
            profilePhoto.setVisible(true);
        } else {
            profilePhoto.setVisible(false);
        }
    }

    public void setCV(String name, String email, String phone, String address, String education, String skills, String workExperience, String projects, Image photo) {
        CVModel cvData = new CVModel(name, email, phone, address, education, skills, workExperience, projects);
        setCVData(cvData, photo);
    }

    @FXML
    private void handleClose() {
        profilePhoto.getScene().getWindow().hide();
    }
}