package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardApplication extends Application {
    private static DashboardApplication instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this; // Set the instance to this DashboardApplication
        loadLoginScene(primaryStage); // Load the login scene initially
    }


    public static void loadLoginScene(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardApplication.class.getResource("hello-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 520, 400);
            primaryStage.setScene(scene);

            primaryStage.setTitle("Login");
            primaryStage.setMaximized(false);
            primaryStage.show();

            HelloController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // Method to launch the dashboard
    public static void launchDashboard() {
        Stage primaryStage = new Stage();
        instance.loadDashboardScene(primaryStage);
    }

    private static void loadDashboardScene(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardApplication.class.getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1480, 783);
            primaryStage.setScene(scene);

            primaryStage.setTitle("Dashboard");
            primaryStage.setMaximized(false);
            primaryStage.show();

            DashboardController controller = loader.getController();
            // Add any additional controller setup if needed

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}