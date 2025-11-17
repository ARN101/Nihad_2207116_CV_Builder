package com.example.cv_builder;

public class CVModel {
    private String fullName, email, phone, address, qualifications, skills, experience, projects;

    // Constructor to initialize all fields
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

    // Getter methods for all fields
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getQualifications() {
        return qualifications;
    }

    public String getSkills() {
        return skills;
    }

    public String getExperience() {
        return experience;
    }

    public String getProjects() {
        return projects;
    }
}
