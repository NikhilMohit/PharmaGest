package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Maintenance extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Maintenance.fxml"));
            Parent root = loader.load();

            // Access the controller to set user type
            MaintenanceController controller = loader.getController();

            // Assuming username is a static field in DashboardController
            String username = DashboardController.username;
            controller.initialize(username);



            // Create a scene with the loaded FXML content
            Scene scene = new Scene(root);

            // Set the scene to the stage
            primaryStage.setScene(scene);

            // Set the title of the window
            primaryStage.setTitle("Maintenance App");
            primaryStage.setMaximized(true);

            // Show the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
