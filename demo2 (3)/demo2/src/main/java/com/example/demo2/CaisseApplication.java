package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CaisseApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Caisse Application");

        initRootLayout();
        showCaisseView();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CaisseApplication.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Set the desired width and height for the root layout
            rootLayout.setPrefWidth(1435);
            rootLayout.setPrefHeight(800);

            Scene scene = new Scene(rootLayout, 1435, 800);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCaisseView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CaisseApplication.class.getResource("Caisse.fxml"));
            AnchorPane caisseView = (AnchorPane) loader.load();

            rootLayout.setCenter(caisseView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
