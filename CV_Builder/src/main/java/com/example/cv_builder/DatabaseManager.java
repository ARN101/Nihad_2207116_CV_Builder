package com.example.cv_builder;

import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cvbuilder.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createTableSQL = """
                        CREATE TABLE IF NOT EXISTS cvs (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            full_name TEXT NOT NULL,
                            email TEXT NOT NULL,
                            phone TEXT NOT NULL,
                            address TEXT,
                            qualifications TEXT,
                            skills TEXT,
                            experience TEXT,
                            projects TEXT,
                            profile_photo BLOB,
                            created_at TEXT NOT NULL,
                            updated_at TEXT NOT NULL
                        )
                    """;

            Statement stmt = conn.createStatement();
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int saveCV(CVModel cvModel, Image profilePhoto) {
        String sql = """
                    INSERT INTO cvs (full_name, email, phone, address, qualifications,
                                   skills, experience, projects, profile_photo, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            pstmt.setString(1, cvModel.getFullName());
            pstmt.setString(2, cvModel.getEmail());
            pstmt.setString(3, cvModel.getPhone());
            pstmt.setString(4, cvModel.getAddress());
            pstmt.setString(5, cvModel.getQualifications());
            pstmt.setString(6, cvModel.getSkills());
            pstmt.setString(7, cvModel.getExperience());
            pstmt.setString(8, cvModel.getProjects());

            if (profilePhoto != null) {
                byte[] imageBytes = imageToBytes(profilePhoto);
                pstmt.setBytes(9, imageBytes);
            } else {
                pstmt.setNull(9, Types.BLOB);
            }

            pstmt.setString(10, timestamp);
            pstmt.setString(11, timestamp);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving CV: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateCV(int id, CVModel cvModel, Image profilePhoto) {
        String sql = """
                    UPDATE cvs SET full_name = ?, email = ?, phone = ?, address = ?,
                                  qualifications = ?, skills = ?, experience = ?, projects = ?,
                                  profile_photo = ?, updated_at = ?
                    WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            pstmt.setString(1, cvModel.getFullName());
            pstmt.setString(2, cvModel.getEmail());
            pstmt.setString(3, cvModel.getPhone());
            pstmt.setString(4, cvModel.getAddress());
            pstmt.setString(5, cvModel.getQualifications());
            pstmt.setString(6, cvModel.getSkills());
            pstmt.setString(7, cvModel.getExperience());
            pstmt.setString(8, cvModel.getProjects());

            if (profilePhoto != null) {
                byte[] imageBytes = imageToBytes(profilePhoto);
                pstmt.setBytes(9, imageBytes);
            } else {
                pstmt.setNull(9, Types.BLOB);
            }

            pstmt.setString(10, timestamp);
            pstmt.setInt(11, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating CV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<CVData> getAllCVs() {
        List<CVData> cvList = new ArrayList<>();
        String sql = "SELECT id, full_name, email, phone, created_at, updated_at FROM cvs ORDER BY updated_at DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CVData cvData = new CVData();
                cvData.setId(rs.getInt("id"));
                cvData.setFullName(rs.getString("full_name"));
                cvData.setEmail(rs.getString("email"));
                cvData.setPhone(rs.getString("phone"));
                cvData.setCreatedAt(rs.getString("created_at"));
                cvData.setUpdatedAt(rs.getString("updated_at"));
                cvList.add(cvData);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CVs: " + e.getMessage());
            e.printStackTrace();
        }
        return cvList;
    }

    public CVModel getCVById(int id) {
        String sql = "SELECT * FROM cvs WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                CVModel cvModel = new CVModel(
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("qualifications"),
                        rs.getString("skills"),
                        rs.getString("experience"),
                        rs.getString("projects"));

                byte[] photoBytes = rs.getBytes("profile_photo");
                if (photoBytes != null) {
                    Image photo = bytesToImage(photoBytes);
                    cvModel.setProfilePhoto(photo);
                }

                return cvModel;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving CV: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteCV(int id) {
        String sql = "DELETE FROM cvs WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting CV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private byte[] imageToBytes(Image image) {
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error converting image to bytes: " + e.getMessage());
            return null;
        }
    }

    private Image bytesToImage(byte[] imageBytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return new Image(bais);
        } catch (Exception e) {
            System.err.println("Error converting bytes to image: " + e.getMessage());
            return null;
        }
    }

    public static class CVData {
        private final javafx.beans.property.IntegerProperty id = new javafx.beans.property.SimpleIntegerProperty();
        private final javafx.beans.property.StringProperty fullName = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty email = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty phone = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty createdAt = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty updatedAt = new javafx.beans.property.SimpleStringProperty();

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public javafx.beans.property.IntegerProperty idProperty() {
            return id;
        }

        public String getFullName() {
            return fullName.get();
        }

        public void setFullName(String fullName) {
            this.fullName.set(fullName);
        }

        public javafx.beans.property.StringProperty fullNameProperty() {
            return fullName;
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        public javafx.beans.property.StringProperty emailProperty() {
            return email;
        }

        public String getPhone() {
            return phone.get();
        }

        public void setPhone(String phone) {
            this.phone.set(phone);
        }

        public javafx.beans.property.StringProperty phoneProperty() {
            return phone;
        }

        public String getCreatedAt() {
            return createdAt.get();
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt.set(createdAt);
        }

        public javafx.beans.property.StringProperty createdAtProperty() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt.get();
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt.set(updatedAt);
        }

        public javafx.beans.property.StringProperty updatedAtProperty() {
            return updatedAt;
        }
    }
}