package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CaisseApplicationController {
    // Reference to your TableView
    @FXML
    private TableView<CaisseItem> caisseTable;
    @FXML
    private TableView<ReceiptItem> caisseitemTable;
    // TableColumn for IdReceipt
    @FXML
    private TableColumn<CaisseItem, String> IdReceiptColumn;
    @FXML
    private TableColumn<CaisseItem, String> VendeurColumn;

    // TableColumn for Total
    @FXML
    private TableColumn<CaisseItem, Double> TotalColumn;
    @FXML
    private TableColumn<ReceiptItem, Integer> itemidreceiptColumn;

    @FXML
    private TableColumn<ReceiptItem, Integer> idmedicamentColumn;
    @FXML
    private TableColumn<ReceiptItem, String> nomColumn;
    @FXML
    private TableColumn<ReceiptItem, Integer> prixColumn;
    @FXML
    private TableColumn<ReceiptItem, Integer> quantiteColumn;
    @FXML
    private TableColumn<ReceiptItem, Double> totalColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label montanttotal;
    @FXML
    private TextField montantpayer;

    @FXML
    private Button PayerButton;

    @FXML
    public void initialize() {

        idmedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("idreceipt"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomreceipt"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixreceipt"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantitereceipt"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));


        // Call populateCaisseTable() during initialization
        populateCaisseTable();
        setupSearchListener();

        // Add a listener to the selection model of caisseTable
        caisseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // When a new item is selected, retrieve its idReceipt
                String selectedIdReceipt = newSelection.getIdReceipt();
                // Populate caisseitemTable with related items based on selectedIdReceipt
                populateCaisseItemTable(selectedIdReceipt);
            }
        });

        // Add a listener to the TableView selection property
        caisseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Get the total from the selected row
                double total = newSelection.getTotal();

                // Update the montanttotal label with the total and euro symbol
                montanttotal.setText("Rs" + String.valueOf(total));

            }
        });
    }

    private void setupSearchListener() {
        // Wrap the ObservableList of items in a FilteredList
        FilteredList<CaisseItem> filteredList = new FilteredList<>(caisseTable.getItems(), p -> true);

        // Add a change listener to the text property of the searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(caisseItem -> {
                if (newValue == null || newValue.isEmpty()) {
                    // If the search text is empty, show all items
                    return true;
                }

                // Convert the search text to lowercase for case-insensitive search
                String searchText = newValue.toLowerCase();

                // Check if the receipt ID or total contains the search text
                return caisseItem.getIdReceipt().toLowerCase().contains(searchText)
                        || String.valueOf(caisseItem.getTotal()).toLowerCase().contains(searchText);
            });
        });

        // Bind the filtered list to the TableView
        caisseTable.setItems(filteredList);
    }

    private void populateCaisseItemTable(String idReceipt) {
        try {
            // Fetch data from the database based on the selected idReceipt
            String query = "SELECT idmedicament, nom,prix , quantite, total\n" +
                    "FROM receipt_items\n" +
                    "WHERE receipt_id = ?";

            // Prepare the statement
            PreparedStatement statement = DBUtil.dbConnect().prepareStatement(query);
            statement.setString(1, idReceipt);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Create ObservableList to hold the data
            ObservableList<ReceiptItem> receiptItems = FXCollections.observableArrayList();

            // Populate the ObservableList with data from the ResultSet
            while (resultSet.next()) {
                int idMedicament = resultSet.getInt("idmedicament");
                String nomMedicament = resultSet.getString("nom");
                Double prix = resultSet.getDouble("prix");
                int quantite = resultSet.getInt("quantite"); // Corrected column name to lowercase
                Double total = resultSet.getDouble("total");

                ReceiptItem item = new ReceiptItem(idMedicament, nomMedicament,prix, quantite, total); // Corrected the constructor arguments
                receiptItems.add(item);
            }

            // Set the items to the caisseitemTable
            caisseitemTable.setItems(receiptItems);

            // Close the ResultSet and the statement after use
            resultSet.close();
            statement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception as per your requirement
        } finally {
            try {
                // Ensure the connection is closed after use
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception as per your requirement
            }
        }
    }



    // Method to retrieve receipts with status "en cours" and populate the TableView
    public void populateCaisseTable() {
        try {
            // Fetch data from the database
            ResultSet resultSet = DBUtil.dbExecuteQuery("SELECT receipt_id, total, vendeur FROM receipt WHERE status = 'en cours'");

            // Create ObservableList to hold the data
            ObservableList<CaisseItem> caisseItems = FXCollections.observableArrayList();

            // Populate the ObservableList with data from the ResultSet
            while (resultSet.next()) {
                String idReceipt = resultSet.getString("receipt_id");
                String vendeur = resultSet.getString("vendeur");
                double total = resultSet.getDouble("total");


                CaisseItem item = new CaisseItem(idReceipt, vendeur, total);
                caisseItems.add(item);
            }

            // Set the items to the TableView
            caisseTable.setItems(caisseItems);

            // Set cell value factories to display data in the columns
            IdReceiptColumn.setCellValueFactory(cellData -> cellData.getValue().idReceiptProperty());
            VendeurColumn.setCellValueFactory(cellData -> cellData.getValue().vendeurProperty());
            TotalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

            // Close the ResultSet (and its Statement) after use
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception as per your requirement
        }
    }
    @FXML
    public void handlePayerButtonAction(ActionEvent event) {
        // Calculate the amount to be returned (if applicable)
        double montantPayer = Double.parseDouble(montantpayer.getText()); // Assuming montantpayer is a TextField containing the amount paid
        double montantTotal = Double.parseDouble(montanttotal.getText().replace("Rs", "")); // Remove the euro symbol before parsing
        double montantRendre =montantPayer - montantTotal;

        // Check if the amount to be returned is greater than or equal to 0
        if (montantRendre >= 0) {
            // Retrieve the selected receipt ID
            CaisseItem selectedCaisseItem = caisseTable.getSelectionModel().getSelectedItem();
            String selectedIdReceipt = selectedCaisseItem.getIdReceipt();



                // Update the status of the selected receipt ID to "Payé" in the database
                updateReceiptStatus(selectedIdReceipt);
                String montantRendreFormatted = String.format("%.2f", montantRendre);
                // Create and configure the alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Transaction réussie");
                alert.setHeaderText("La transaction a bien été effectuée");
                alert.setContentText("Montant à rendre: Rs" + montantRendreFormatted);

                // Show the alert
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Clear the items in the caisseitemTable
                    caisseitemTable.getItems().clear();
                    // Refresh the page content
                    refreshPageContent();
                }
            });

        } else {
            // If the amount to be returned is negative, display an error
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Montant incorrect");
            alert.setContentText("Le montant à payer est inférieur au montant total.");

            // Show the alert
            alert.showAndWait();
        }
    }

    private void updateReceiptStatus(String idReceipt) {
        try {
            // Prepare the update statement to set the status to "Payé" for the given receipt ID
            String updateQuery = "UPDATE receipt SET status = 'Payé' WHERE receipt_id = ?;" +
                    "UPDATE receipt_items SET status = 'Payé' WHERE receipt_id = ?;";
            PreparedStatement updateStatement = DBUtil.dbConnect().prepareStatement(updateQuery);
            updateStatement.setString(1, idReceipt);
            updateStatement.setString(2, idReceipt);

            // Execute the update statement
            updateStatement.executeUpdate();

            // Close the statement
            updateStatement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception as per your requirement
        }
    }


    private void refreshPageContent() {
        // Here you can add the logic to refresh your page content.
        // For example, you can clear the text fields, reset the table views, etc.
        montantpayer.clear(); // Clear the text field for amount paid
        // You may need to repopulate the tables if needed
        populateCaisseTable(); // Example method call to repopulate the table
        // You can add additional refresh logic as per your requirements
    }
}
