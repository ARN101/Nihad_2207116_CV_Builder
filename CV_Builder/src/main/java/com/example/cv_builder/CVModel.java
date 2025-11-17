package com.example.cv_builder;

import javafx.scene.image.Image;

public class CVModel {
    private String fullName, email, phone, address, qualifications, skills, experience, projects;
    private Image profilePhoto;

    public CVModel(String fullName, String email, String phone, String address,
                   String qualifications, String skills, String experience, String projects) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.qualifications = qualifications;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public Image getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Image profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}