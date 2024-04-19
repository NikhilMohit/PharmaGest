package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.demo2.Fournisseur;
import com.example.demo2.FournisseurDAO;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FournisseurMaintenanceController {

    @FXML
    private TextField fournIdText;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextField newfournisseurText;
    @FXML
    private TextField newnomfournisseurText;
    @FXML
    private TextField newadressefournisseurText;
    @FXML
    private TextField newcontactfournisseurText;
    @FXML
    private TextField newmailfournisseurText;
    @FXML
    private TableView fournisseurTable;
    @FXML
    private TableColumn<Fournisseur, Integer> fournIdColumn;
    @FXML
    private TableColumn<Fournisseur, String> fournisseurColumn;

    @FXML
    private TableColumn<Fournisseur, String> addressefournColumn;
    @FXML
    private TableColumn<Fournisseur, String> contactfournColumn;
    @FXML
    private TableColumn<Fournisseur, String> emailfournColumn;
    @FXML
    private ChoiceBox<String> updateFournisseurFieldChoiceBox;

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

        fournIdColumn.setCellValueFactory(cellData -> cellData.getValue().idfournisseurProperty().asObject());
        fournisseurColumn.setCellValueFactory(cellData -> cellData.getValue().nomfournisseurProperty());
        addressefournColumn.setCellValueFactory(cellData -> cellData.getValue().adressefournisseurProperty());
        contactfournColumn.setCellValueFactory(cellData -> cellData.getValue().contactfournisseurProperty());
        emailfournColumn.setCellValueFactory(cellData -> cellData.getValue().emailfournisseurProperty());

        try {
            ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
            populateFournisseurs(allFournisseurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
        updateFournisseurFieldChoiceBox.setValue("Nouveau Nom");
    }



    // Search a utilisateur
    @FXML
    private void searchFournisseur(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            // Get Utilisateur information
            Fournisseur fournisseur = FournisseurDAO.searchFournisseur(fournIdText.getText());
            // Populate Utilisateur on TableView and Display on TextArea
            populateAndShowFournisseur(fournisseur);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error occurred while getting fournisseur information from DB.\n" + e);
            throw e;
        }
    }

    // Search all utilisateurs


    // Populate Utilisateur for TableView
    @FXML
    private void populateFournisseur(Fournisseur fournisseur) throws ClassNotFoundException {
        // Declare and ObservableList for table view
        ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();
        // Add utilisateur to the ObservableList
        fournisseurData.add(fournisseur);
        // Set items to the utilisateurTable
        fournisseurTable.setItems(fournisseurData);
    }

    // Set Utilisateur information to Text Area
    @FXML
    private void setFournisseurInfoToTextArea(Fournisseur fournisseur) {
        resultArea.setText("Nom: " + fournisseur.getnomfournisseur() + "\n" +
                "Adresse: " + fournisseur.getadressefournisseur() + "\n" +
                "Contact: " + fournisseur.getcontactfournisseur());
    }

    // Populate Utilisateur for TableView and Display Utilisateur on TextArea
    @FXML
    private void populateAndShowFournisseur(Fournisseur fournisseur) throws ClassNotFoundException {
        if (fournisseur != null) {
            populateFournisseur(fournisseur);
            setFournisseurInfoToTextArea(fournisseur);
        } else {
            resultArea.setText("This fournisseur does not exist!\n");
        }
    }

    // Populate Utilisateurs for TableView
    @FXML
    private void populateFournisseurs(ObservableList<Fournisseur> fournisseurData) throws ClassNotFoundException {
        // Set items to the utilisateurTable
        fournisseurTable.setItems(fournisseurData);
    }

    // Update utilisateur's email with the email which is written on newEmailText field
    @FXML
    private void updateFournisseurField(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Convert userIdText.getText() to int
            int fournId = Integer.parseInt(fournIdText.getText());

            String selectedField = updateFournisseurFieldChoiceBox.getValue();
            String newValue = newfournisseurText.getText();

            switch (selectedField) {
                case "Nouveau Nom":
                    FournisseurDAO.updateFournisseurNom(fournId, newValue);
                    resultArea.setText("Nom has been updated for fournisseur id: " + fournId + "\n");
                    break;
                case "Nouvelle Adresse":
                    FournisseurDAO.updateFournisseurAdresse(fournId, newValue);
                    resultArea.setText("Adresse has been updated for fournisseur id: " + fournId + "\n");
                    break;
                case "Nouveau Contact":
                    FournisseurDAO.updateFournisseurContact(fournId, newValue);
                    resultArea.setText("Contact has been updated for fournisseur id: " + fournId + "\n");
                    break;
                case "Nouveau Mail":
                    FournisseurDAO.updateFournisseurMail(fournId, newValue);
                    resultArea.setText("Email has been updated for fournisseur id: " + fournId + "\n");
                    break;
                default:
                    resultArea.setText("Invalid selection");
            }
        } catch (NumberFormatException | SQLException e) {
            resultArea.setText("Problem occurred while updating field: " + e);
        }
        try {
            ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
            populateFournisseurs(allFournisseurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    // Insert a utilisateur to the DB
    @FXML
    private void insertFournisseur(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            FournisseurDAO.insertFournisseur(newnomfournisseurText.getText(), newadressefournisseurText.getText(), newcontactfournisseurText.getText(),newmailfournisseurText.getText());
            resultArea.setText("Fournisseur inserted! \n");
        } catch (SQLException e) {
            resultArea.setText("Problem occurred while inserting Fournisseur " + e);
            throw e;
        }
        try {
            ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
            populateFournisseurs(allFournisseurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    @FXML
    private void supprimerFournisseur(ActionEvent event) {
        // Check if the medicament ID text field is empty
        if (!fournIdText.getText().isEmpty()) {
            // If the text field is not empty, use the medicament ID from the text field for deletion
            String fournId = fournIdText.getText();

            try {
                // Delete the medicament with the provided ID
                FournisseurDAO.deletefournisseurwithid(fournId);
                resultArea.setText("Medicament deleted! Medicament id: " + fournId + "\n");

                // Refresh the TableView after deletion
                ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
                populateFournisseurs(allFournisseurs);
            } catch (SQLException | ClassNotFoundException e) {
                resultArea.setText("Problem occurred while deleting fournisseur " + e);
                e.printStackTrace();
                // Handle the exception according to your application's logic
            }
        } else {
            // If the text field is empty, use the selected item from the TableView for deletion
            Fournisseur selectedFournisseur = (Fournisseur) fournisseurTable.getSelectionModel().getSelectedItem();

            if (selectedFournisseur != null) {
                String fournId = String.valueOf(selectedFournisseur.getidfournisseur());

                try {
                    // Delete the medicament with the ID from the selected row
                    FournisseurDAO.deletefournisseurwithid(fournId);
                    resultArea.setText("Fournisseur deleted! Fournisseur id: " + fournId + "\n");

                    // Refresh the TableView after deletion
                    ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
                    populateFournisseurs(allFournisseurs);
                } catch (SQLException | ClassNotFoundException e) {
                    resultArea.setText("Problem occurred while deleting fournisseur " + e);
                    e.printStackTrace();
                    // Handle the exception according to your application's logic
                }
            } else {
                // Inform the user to either select a medicament from the TableView or enter a medicament ID
                resultArea.setText("Please select a fournisseur from the table or enter a fournisseur ID.");
            }
        }
        try {
            ObservableList<Fournisseur> allFournisseurs = FournisseurDAO.searchAllFournisseurs();
            populateFournisseurs(allFournisseurs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }


    @FXML
    private void searchFournisseurs(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Get all fournisseurs information
            ObservableList<Fournisseur> fournisseurData = FournisseurDAO.searchAllFournisseurs();
            // Populate fournisseurs on TableView
            populateFournisseurs(fournisseurData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting fournisseurs information from DB.\n" + e);
            throw e;
        }
    }

}
