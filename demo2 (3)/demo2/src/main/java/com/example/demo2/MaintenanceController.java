package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.demo2.DashboardController.username;

public class MaintenanceController {

    private String userType = "User";
    private Stage primaryStage;

    @FXML
    private Button utilisateurButton;
    @FXML
    private Button FournisseurButton;
    @FXML
    private Button MedicamentButton;

    @FXML
    private Label usernameLabel;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private String getUserTypeFromDatabase(String username) {
        try (Connection connectDB = DatabaseConnection.getConnection("Pharmagest", "postgres", "Nmohit03042004@")) {
            String userTypeQuery = "SELECT permission FROM useraccounts WHERE username = ?";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(userTypeQuery)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("permission");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User";
    }

    public void initialize(String username) {
        System.out.println("Username: " + username);
        String userTypeFromDB = getUserTypeFromDatabase(username);
        System.out.println("User type from DB: " + userTypeFromDB);
        setUserType(userTypeFromDB);
        usernameLabel.setText("Username: " + username);

        FournisseurButton.setOnAction(event -> openFournisseurMaintenanceScene());
        MedicamentButton.setOnAction(event -> openMedicamentMaintenanceScene());
    }

    public void setUserType(String userType) {
        this.userType = userType;
        updateUI();
    }

    private void updateUI() {
        System.out.println("Type d'utilisateur : " + userType);

        if ("SU".equals(userType)) {
            System.out.println("Afficher le bouton utilisateur.");
            utilisateurButton.setVisible(true);
            utilisateurButton.setOnAction(event -> openUtilisateurMaintenanceScene());
        } else {
            System.out.println("Masquer le bouton utilisateur.");
            utilisateurButton.setVisible(false);
        }
    }

    private void openUtilisateurMaintenanceScene() {
        try {
            UtilisateurMaintenance utilisateurMaintenance = new UtilisateurMaintenance();
            utilisateurMaintenance.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFournisseurMaintenanceScene() {
        try {
            FournisseurMaintenance fournisseurMaintenance = new FournisseurMaintenance();
            fournisseurMaintenance.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMedicamentMaintenanceScene() {
        try {
            MedicamentMaintenance medicamentMaintenance = new MedicamentMaintenance();
            medicamentMaintenance.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetourButton(ActionEvent event) {
        try {
            // Close the Maintenance stage
            Stage maintenanceStage = (Stage) usernameLabel.getScene().getWindow();
            maintenanceStage.close();

            // Load the Dashboard scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1900, 558);
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(scene);
            dashboardStage.setTitle("Dashboard");
            dashboardStage.setMaximized(true);
            dashboardStage.show();

            DashboardController controller = loader.getController();
            controller.setPrimaryStage(dashboardStage);
            controller.setUsername(username); // Pass the username
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Dashboard.fxml: " + e.getMessage());
        }
    }

}
