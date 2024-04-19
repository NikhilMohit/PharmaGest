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

public class MedicamentMaintenanceController {

    @FXML
    private TextField medicamentIdText;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextField newmedicamentText;
    @FXML
    private TextField newnommedicamentText;
    @FXML
    private TextField newfournisseurmedicamentText;
    @FXML
    private TextField newfamillemedicamentText;
    @FXML
    private TextField newformemedicamentText;
    @FXML
    private TextField newquantitemincommandemedicamentText;
    @FXML
    private TextField newquantitemaxstockmedicamentText;
    @FXML
    private TextField newquantiteenstockmedicamentText;
    @FXML
    private ChoiceBox<String> newordonnancemedicamentText;
    @FXML
    private TableView medicamentTable;
    @FXML
    private TableColumn<Medicament, Integer> medicamentIdColumn;
    @FXML
    private TableColumn<Medicament, String> NomMedicamentColumn;

    @FXML
    private TableColumn<Medicament, String> FournisseurMedicamentColumn;
    @FXML
    private TableColumn<Medicament, String> FamilleMedicamentColumn;
    @FXML
    private TableColumn<Medicament, String> FormeMedicamentColumn;
    @FXML
    private TableColumn<Medicament, Integer> QuantiteMinCommandeMedicamentColumn;

    @FXML
    private TableColumn<Medicament, Integer> QuantiteMaxStockMedicamentColumn;
    @FXML
    private TableColumn<Medicament, Integer> QuantiteEnStockMedicamentColumn;
    @FXML
    private TableColumn<Medicament, Boolean> OrdonnanceMedicamentColumn;
    @FXML
    private ChoiceBox<String> updateMedicamentFieldChoiceBox;

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

        medicamentIdColumn.setCellValueFactory(cellData -> cellData.getValue().idmedicamentProperty().asObject());
        NomMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().nommedicamentProperty());
        FournisseurMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().fournisseurmedicamentProperty());
        FamilleMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().famillemedicamentProperty());
        FormeMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().formemedicamentProperty());
        QuantiteMinCommandeMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().quantitemincommandemedicamentProperty().asObject());
        QuantiteMaxStockMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().quantitemaxstockmedicamentProperty().asObject());
        QuantiteEnStockMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteenstockmedicamentProperty().asObject());
        OrdonnanceMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().ordonnancemedicamentProperty());

        try {
            ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
            populateMedicaments(allMedicaments);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
        updateMedicamentFieldChoiceBox.setValue("Nouveau Nom");
    }



    // Search a utilisateur
    @FXML
    private void searchMedicament(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            // Get Utilisateur information
            Medicament medicament = MedicamentDAO.searchMedicament(medicamentIdText.getText());
            // Populate Utilisateur on TableView and Display on TextArea
            populateAndShowMedicament(medicament);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error occurred while getting medicament information from DB.\n" + e);
            throw e;
        }
    }

    // Search all utilisateurs


    // Populate Utilisateur for TableView
    @FXML
    private void populateMedicament(Medicament medicament) throws ClassNotFoundException {
        // Declare and ObservableList for table view
        ObservableList<Medicament> medicamentData = FXCollections.observableArrayList();
        // Add utilisateur to the ObservableList
        medicamentData.add(medicament);
        // Set items to the utilisateurTable
        medicamentTable.setItems(medicamentData);
    }

    // Set Utilisateur information to Text Area
    @FXML
    private void setMedicamentInfoToTextArea(Medicament medicament) {
        resultArea.setText("Medicament: " + medicament.getnommedicament() + "\n" +
                "Fournisseur: " + medicament.getfournisseurmedicament() + "\n" +
                "Famille: " + medicament.getfamillemedicament());
    }

    // Populate Utilisateur for TableView and Display Utilisateur on TextArea
    @FXML
    private void populateAndShowMedicament(Medicament medicament) throws ClassNotFoundException {
        if (medicament != null) {
            populateMedicament(medicament);
            setMedicamentInfoToTextArea(medicament);
        } else {
            resultArea.setText("This medicament does not exist!\n");
        }
    }

    // Populate Utilisateurs for TableView
    @FXML
    private void populateMedicaments(ObservableList<Medicament> medicamentData) throws ClassNotFoundException {
        // Set items to the utilisateurTable
        medicamentTable.setItems(medicamentData);
    }

    // Update utilisateur's email with the email which is written on newEmailText field
    @FXML
    private void updateMedicamentField(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Convert userIdText.getText() to int
            int medicamentId = Integer.parseInt(medicamentIdText.getText());

            String selectedField = updateMedicamentFieldChoiceBox.getValue();
            String newValue = newmedicamentText.getText();

            switch (selectedField) {
                case "Nouveau Nom":
                    MedicamentDAO.updateMedicamentNom(medicamentId, newValue);
                    resultArea.setText("Nom has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouveau Fournisseur":
                    MedicamentDAO.updateMedicamentFournisseur(medicamentId, newValue);
                    resultArea.setText("Fournisseur has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle Famille":
                    MedicamentDAO.updateMedicamentFamille(medicamentId, newValue);
                    resultArea.setText("Famille has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle Forme":
                    MedicamentDAO.updateMedicamentForme(medicamentId, newValue);
                    resultArea.setText("Forme has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle quantitemincomm":
                    MedicamentDAO.updateMedicamentQuantiteMinCommande(medicamentId, newValue);
                    resultArea.setText("QuantiteMinComm has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle quantitemaxstock":
                    MedicamentDAO.updateMedicamentQuantiteMaxStock(medicamentId, newValue);
                    resultArea.setText("quantitemaxstock has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle quantiteenstock":
                    MedicamentDAO.updateMedicamentQuantiteEnStock(medicamentId, newValue);
                    resultArea.setText("quantiteenstock has been updated for medicament id: " + medicamentId + "\n");
                    break;
                case "Nouvelle Ordonnance":
                    MedicamentDAO.updateMedicamentOrdonnance(medicamentId, newValue);
                    resultArea.setText("Ordonnance has been updated for medicament id: " + medicamentId + "\n");
                    break;
                default:
                    resultArea.setText("Invalid selection");
            }
        } catch (NumberFormatException | SQLException e) {
            resultArea.setText("Problem occurred while updating field: " + e);
        }
        try {
            ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
            populateMedicaments(allMedicaments);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    @FXML
    private Button supprimerButton; // Assuming you have a button named "supprimerButton" in your FXML file.


    @FXML
    private void supprimerMedicament(ActionEvent event) {
        // Check if the medicament ID text field is empty
        if (!medicamentIdText.getText().isEmpty()) {
            // If the text field is not empty, use the medicament ID from the text field for deletion
            String medicamentId = medicamentIdText.getText();

            try {
                // Delete the medicament with the provided ID
                MedicamentDAO.deletemedicamentwithid(medicamentId);
                resultArea.setText("Medicament deleted! Medicament id: " + medicamentId + "\n");

                // Refresh the TableView after deletion
                ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
                populateMedicaments(allMedicaments);
            } catch (SQLException | ClassNotFoundException e) {
                resultArea.setText("Problem occurred while deleting medicament " + e);
                e.printStackTrace();
                // Handle the exception according to your application's logic
            }
        } else {
            // If the text field is empty, use the selected item from the TableView for deletion
            Medicament selectedMedicament = (Medicament) medicamentTable.getSelectionModel().getSelectedItem();

            if (selectedMedicament != null) {
                String medicamentId = String.valueOf(selectedMedicament.getidmedicament());

                try {
                    // Delete the medicament with the ID from the selected row
                    MedicamentDAO.deletemedicamentwithid(medicamentId);
                    resultArea.setText("Medicament deleted! Medicament id: " + medicamentId + "\n");

                    // Refresh the TableView after deletion
                    ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
                    populateMedicaments(allMedicaments);
                } catch (SQLException | ClassNotFoundException e) {
                    resultArea.setText("Problem occurred while deleting medicament " + e);
                    e.printStackTrace();
                    // Handle the exception according to your application's logic
                }
            } else {
                // Inform the user to either select a medicament from the TableView or enter a medicament ID
                resultArea.setText("Please select a medicament from the table or enter a medicament ID.");
            }
        }
    }



    // Insert a utilisateur to the DB
    @FXML
    private void insertMedicament(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Convert text inputs to appropriate data types
            String nommedicament = newnommedicamentText.getText();
            String fournisseurmedicament = newfournisseurmedicamentText.getText();
            String famillemedicament = newfamillemedicamentText.getText();
            String formemedicament = newformemedicamentText.getText();
            int quantitemincommandemedicament = Integer.parseInt(newquantitemincommandemedicamentText.getText());
            int quantitemaxstockmedicament = Integer.parseInt(newquantitemaxstockmedicamentText.getText());
            int quantiteenstockmedicament = Integer.parseInt(newquantiteenstockmedicamentText.getText());
            boolean ordonnancemedicament = Boolean.parseBoolean(newordonnancemedicamentText.getValue());

            // Check if the fournisseur exists
            boolean fournisseurExists = MedicamentDAO.fournisseurExists(fournisseurmedicament);
            if (!fournisseurExists) {
                String errorMessage = "The fournisseur '" + fournisseurmedicament + "' does not exist. Medicament not inserted.";
                resultArea.setText(errorMessage);
                return;
            }

            // Call the DAO method with the converted values
            MedicamentDAO.insertMedicament(nommedicament, fournisseurmedicament, famillemedicament, formemedicament, quantitemincommandemedicament, quantitemaxstockmedicament, quantiteenstockmedicament, ordonnancemedicament);

            // Display success message only if insertion was successful
            resultArea.setText("Medicament inserted!\n");

            // Clear input fields after successful insertion
            clearInputFields();
        } catch (NumberFormatException e) {
            resultArea.setText("Error: Please enter valid numeric values for quantity fields.\n" + e);
        } catch (SQLException e) {
            resultArea.setText("Problem occurred while inserting Medicament: " + e.getMessage());
            throw e;
        }
        try {
            ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
            populateMedicaments(allMedicaments);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    // Helper method to clear input fields after successful insertion
    private void clearInputFields() {
        newnommedicamentText.clear();
        newfournisseurmedicamentText.clear();
        newfamillemedicamentText.clear();
        newformemedicamentText.clear();
        newquantitemincommandemedicamentText.clear();
        newquantitemaxstockmedicamentText.clear();
        newquantiteenstockmedicamentText.clear();
        newordonnancemedicamentText.setValue(null);
    }



    // Delete a utilisateur with a given utilisateur Id from DB
    @FXML
    private void deleteMedicament(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            MedicamentDAO.deletemedicamentwithid(medicamentIdText.getText());
            resultArea.setText("Medicament deleted! Medicament id: " + medicamentIdText.getText() + "\n");
        } catch (SQLException e) {
            resultArea.setText("Problem occurred while deleting medicament " + e);
            throw e;
        }
        try {
            ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
            populateMedicaments(allMedicaments);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }
    }

    @FXML
    private void searchMedicaments(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            // Get all medicaments information
            ObservableList<Medicament> medicamentData = MedicamentDAO.searchAllMedicaments();
            // Populate medicaments on TableView
            populateMedicaments(medicamentData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting medicaments information from DB.\n" + e);
            throw e;
        }
    }

}
