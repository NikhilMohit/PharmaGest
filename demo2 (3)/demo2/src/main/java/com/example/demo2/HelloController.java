package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HelloController {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;



    @FXML
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    @FXML
    public void setLoginButtonOnAction(ActionEvent e) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        if (username.isBlank() || password.isBlank()) {
            loginMessageLabel.setText("Entrez un nom d'utilisateur et mot de passe");
            return;
        }

        if (validateLogin(username, password)) {

            openDashboard(username);
        } else {
            loginMessageLabel.setText("Essayez à nouveau");
        }
    }


    public void setCancelButtonOnAction(ActionEvent e) {
        closeWindow(cancelButton);
    }

    private boolean validateLogin(String username, String password) {
        try (Connection connectDB = DatabaseConnection.getConnection("Pharmagest", "postgres", "Nmohit03042004@")) {
            String verifyLogin = "SELECT firstname, lastname FROM useraccounts WHERE username = ? AND mdp_pharm = ?";

            try (PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet queryResult = preparedStatement.executeQuery()) {
                    if (queryResult.next()) {
                        String firstName = queryResult.getString("firstname");
                        String lastName = queryResult.getString("lastname");
                        String fullName = firstName + " " + lastName;

                        updateLoginSuccess(username);

                        // You can return the full name here if needed
                        return true;
                    } else {
                        recordFailedLogin(username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    private void updateLoginSuccess(String username) {
        try (Connection connectDB = DatabaseConnection.getConnection("Pharmagest", "postgres", "Nmohit03042004@")) {
            String updateQuery = "UPDATE useraccounts SET last_login = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                preparedStatement.setTimestamp(1, currentTimestamp);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordFailedLogin(String username) {
        try (Connection connectDB = DatabaseConnection.getConnection("Pharmagest", "postgres", "Nmohit03042004@")) {
            String insertLog = "INSERT INTO user_logs (username, login_time, login_sucessful) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertLog)) {
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                preparedStatement.setString(1, username);
                preparedStatement.setTimestamp(2, currentTimestamp);
                preparedStatement.setBoolean(3, false);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1900, 558);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Dashboard");
            primaryStage.setMaximized(true);
            primaryStage.show();

            DashboardController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsername(username); // Pass the username

            // Pass the username to the Maintenance class

            controller.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Dashboard.fxml: " + e.getMessage());
        }
    }



    private void closeWindow(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
