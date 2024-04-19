package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DashboardController {
    private Stage primaryStage;
    private com.example.demo2.DashboardApplication DashboardApplication;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    @FXML
    private Label welcomeLabel; // Assuming you have a Label with this id in your FXML
    @FXML
    private Button Vente;
    @FXML
    private Button ListedesPrix;
    @FXML
    private Button Caisse;




    public static String username; // Add a field to store the username

    public void initialize() {

        Vente.setOnAction(event -> openVenteScene());
        ListedesPrix.setOnAction(event -> openListedesPrixApplicationScene());
        Caisse.setOnAction(event -> openCaisseApplicationScene());

    }


        public void setUsername(String username) {
        this.username = username;
        // Retrieve the full name from the database using the username and set it in the welcomeLabel
        String fullName = retrieveFullName(username);
        welcomeLabel.setText("Bienvenue " + fullName);
    }

    private String retrieveFullName(String username) {
        String fullName = null;

        try (Connection connectDB = DatabaseConnection.getConnection("Pharmagest", "postgres", "Nmohit03042004@")) {
            String selectQuery = "SELECT firstname, lastname FROM useraccounts WHERE username = ?";

            try (PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String firstName = resultSet.getString("firstname");
                        String lastName = resultSet.getString("lastname");
                        fullName = firstName + " " + lastName;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fullName;
    }





    @FXML
    private void disconnect() {
        if (primaryStage != null) {
            // Close the dashboard stage (which is the primary stage)

            primaryStage.close();

            // Call loadLoginScene method from DashboardApplication
            DashboardApplication.loadLoginScene(primaryStage);
        }
    }


    @FXML
    private void openMaintenanceScene(ActionEvent event) {
        try {
            // Load the FXML file for Maintenance
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Maintenance.fxml"));
            Parent maintenanceRoot = loader.load();

            // Access the controller to set the username
            MaintenanceController maintenanceController = loader.getController();
            maintenanceController.initialize(DashboardController.username);

            // Create a new stage for Maintenance
            Stage maintenanceStage = new Stage();
            maintenanceStage.setTitle("Maintenance App");
            maintenanceStage.setMaximized(true);

            // Set the scene to the new stage
            Scene maintenanceScene = new Scene(maintenanceRoot);
            maintenanceStage.setScene(maintenanceScene);

            // Show the Maintenance stage
            maintenanceStage.show();

            // Close the Dashboard stage (which is the primary stage)
            if (primaryStage != null) {
                primaryStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openVenteScene() {
        try {
            VenteApplication venteApplication = new VenteApplication();
            venteApplication.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openListedesPrixApplicationScene() {
        try {
            ListedesPrixApplication listedesPrixApplication = new ListedesPrixApplication();
            listedesPrixApplication.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCaisseApplicationScene() {
        try {
            CaisseApplication caisseApplication = new CaisseApplication();
            caisseApplication.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}

