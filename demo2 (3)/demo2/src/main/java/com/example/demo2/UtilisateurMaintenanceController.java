package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.demo2.Utilisateur;
import com.example.demo2.UtilisateurDAO;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class UtilisateurMaintenanceController {

    @FXML
    private TextField userIdText;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextField newusernameText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField firstnameText;
    @FXML
    private TextField lastnameText;
    @FXML
    private TextField mdp_pharmText;
    @FXML
    private TableView utilisateurTable;
    @FXML
    private TableColumn<Utilisateur, Integer> userIdColumn;
    @FXML
    private TableColumn<Utilisateur, String> usernameColumn;
    @FXML
    private TableColumn<Utilisateur, String> firstNameColumn;
    @FXML
    private TableColumn<Utilisateur, String> lastNameColumn;
    @FXML
    private TableColumn<Utilisateur, String> passwordColumn;
    @FXML
    private TableColumn<Utilisateur, Timestamp> lastLoginColumn;
    @FXML
    private TableColumn<Utilisateur, String> permissionColumn;
    @FXML
    private ChoiceBox<String> updateFieldChoiceBox;

    // For MultiThreading
    private Executor exec;

    // Initializing the controller class.
    // This method is automatically called after the fxml file has been loaded.
    @FXML
    private void initialize() {
        /*
        The setCellValueFactory(...) that we set on the table columns are used to determine
        which field inside the Utilisateur objects should be used for the particular column.
        The arrow -> indicates that we're using a Java 8 feature called Lambdas.
        (Another option would be to use a PropertyValueFactory, but this is not type-safe)

        We're only using StringProperty values for our table columns in this example.
        When you want to use IntegerProperty or DoubleProperty, the setCellValueFactory(...)
        must have an additional asObject():
        */

        // For multithreading: Create executor that uses daemon threads:
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().idUserAccountsProperty().asObject());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstnameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().mdpPharmProperty());
        lastLoginColumn.setCellValueFactory(cellData -> cellData.getValue().lastLoginProperty());
        permissionColumn.setCellValueFactory(cellData -> cellData.getValue().permissionProperty());

        try {
            ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
            populateUtilisateurs(allUtilisateurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
        updateFieldChoiceBox.setValue("Nouveau Nom");
    }

    // Search a utilisateur
    @FXML
    private void searchUtilisateur(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            // Get Utilisateur information
            Utilisateur utilisateur = UtilisateurDAO.searchUtilisateur(userIdText.getText());
            // Populate Utilisateur on TableView and Display on TextArea
            populateAndShowUtilisateur(utilisateur);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error occurred while getting utilisateur information from DB.\n" + e);
            throw e;
        }
    }

    // Search all utilisateurs
    @FXML
    private void searchUtilisateurs(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Get only active Utilisateurs information
            ObservableList<Utilisateur> utilisateurData = UtilisateurDAO.searchActiveUtilisateurs();
            // Populate Utilisateurs on TableView
            populateUtilisateurs(utilisateurData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting utilisateurs information from DB.\n" + e);
            throw e;
        }
    }

    // Populate Utilisateur for TableView
    @FXML
    private void populateUtilisateur(Utilisateur utilisateur) throws ClassNotFoundException {
        // Declare and ObservableList for table view
        ObservableList<Utilisateur> utilisateurData = FXCollections.observableArrayList();
        // Add utilisateur to the ObservableList
        utilisateurData.add(utilisateur);
        // Set items to the utilisateurTable
        utilisateurTable.setItems(utilisateurData);
    }

    // Set Utilisateur information to Text Area
    @FXML
    private void setUtilisateurInfoToTextArea(Utilisateur utilisateur) {
        resultArea.setText("Username: " + utilisateur.getUsername() + "\n" +
                "First Name: " + utilisateur.getFirstname() + "\n" +
                "Last Name: " + utilisateur.getLastname());
    }

    // Populate Utilisateur for TableView and Display Utilisateur on TextArea
    @FXML
    private void populateAndShowUtilisateur(Utilisateur utilisateur) throws ClassNotFoundException {
        if (utilisateur != null) {
            populateUtilisateur(utilisateur);
            setUtilisateurInfoToTextArea(utilisateur);
        } else {
            resultArea.setText("This utilisateur does not exist!\n");
        }
    }

    // Populate Utilisateurs for TableView
    @FXML
    private void populateUtilisateurs(ObservableList<Utilisateur> utilisateurData) throws ClassNotFoundException {
        // Set items to the utilisateurTable
        utilisateurTable.setItems(utilisateurData);
    }

    // Update utilisateur's email with the email which is written on newEmailText field
    @FXML
    private void updateUtilisateurField(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Convert userIdText.getText() to int
            int userId = Integer.parseInt(userIdText.getText());

            String selectedField = updateFieldChoiceBox.getValue();
            String newValue = newusernameText.getText();

            switch (selectedField) {
                case "Nouveau Nom":
                    UtilisateurDAO.updateUtilisateurNom(userId, newValue);
                    resultArea.setText("Nom has been updated for utilisateur id: " + userId + "\n");
                    break;
                case "Nouveau Prénom":
                    UtilisateurDAO.updateUtilisateurPrenom(userId, newValue);
                    resultArea.setText("Prénom has been updated for utilisateur id: " + userId + "\n");
                    break;
                case "Nouveau Identifiant":
                    UtilisateurDAO.updateUtilisateurIdentifiant(userId, newValue);
                    resultArea.setText("Identifiant has been updated for utilisateur id: " + userId + "\n");
                    break;
                default:
                    resultArea.setText("Invalid selection");
            }
        } catch (NumberFormatException | SQLException e) {
            resultArea.setText("Problem occurred while updating field: " + e);
        }
        try {
            ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
            populateUtilisateurs(allUtilisateurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    // Insert a utilisateur to the DB
    @FXML
    private void insertUtilisateur(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            UtilisateurDAO.insertUtilisateur(firstnameText.getText(), lastnameText.getText(), usernameText.getText(),mdp_pharmText.getText());
            resultArea.setText("Utilisateur inserted! \n");
        } catch (SQLException e) {
            resultArea.setText("Problem occurred while inserting utilisateur " + e);
            throw e;
        }
        try {
            ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
            populateUtilisateurs(allUtilisateurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    @FXML
    private void supprimerUtilisateur(ActionEvent event) {
        // Check if the medicament ID text field is empty
        if (!userIdText.getText().isEmpty()) {
            // If the text field is not empty, use the medicament ID from the text field for deletion
            String userId = userIdText.getText();

            try {
                // Delete the medicament with the provided ID
                UtilisateurDAO.deactivateUserWithId(userId);
                resultArea.setText("Utilisateur deleted! Utilisateur id: " + userId + "\n");

                // Refresh the TableView after deletion
                ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
                populateUtilisateurs(allUtilisateurs);
            } catch (SQLException | ClassNotFoundException e) {
                resultArea.setText("Problem occurred while deleting utilisateur " + e);
                e.printStackTrace();
                // Handle the exception according to your application's logic
            }
        } else {
            // If the text field is empty, use the selected item from the TableView for deletion
            Utilisateur selectedUtilisateur = (Utilisateur) utilisateurTable.getSelectionModel().getSelectedItem();

            if (selectedUtilisateur != null) {
                String userId = String.valueOf(selectedUtilisateur.getIdUserAccounts());

                try {
                    // Delete the medicament with the ID from the selected row
                    UtilisateurDAO.deactivateUserWithId(userId);
                    resultArea.setText("Utilisateur deleted! Fournisseur id: " + userId + "\n");

                    // Refresh the TableView after deletion
                    ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
                    populateUtilisateurs(allUtilisateurs);
                } catch (SQLException | ClassNotFoundException e) {
                    resultArea.setText("Problem occurred while deleting utilisateur " + e);
                    e.printStackTrace();
                    // Handle the exception according to your application's logic
                }
            } else {
                // Inform the user to either select a medicament from the TableView or enter a medicament ID
                resultArea.setText("Please select a utilisateur from the table or enter a utilisateur ID.");
            }
        }
        try {
            ObservableList<Utilisateur> allUtilisateurs = UtilisateurDAO.searchActiveUtilisateurs();
            populateUtilisateurs(allUtilisateurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

}
