package com.example.demo2;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.DoubleStringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.demo2.DashboardController.username;

public class VenteApplicationController {
    @FXML
    private TableView medicamentTable;
    @FXML
    private Label error;
    @FXML
    private Button imprimerButton;
    @FXML
    private Button toutsupprimerButton;

    @FXML
    private TableView<ReceiptItem> receiptTable;
    FilteredList<Medicament> filteredData;
    @FXML
    private TableColumn<Medicament, Integer> medicamentIdColumn;
    @FXML
    private TableColumn<Medicament, String> NomMedicamentColumn;


    @FXML
    private TableColumn<Medicament, Double> PrixColumn;

    @FXML
    private TableColumn<Medicament, Integer> QuantiteEnStockMedicamentColumn;
    @FXML
    private TableColumn<Medicament, Boolean> OrdonnanceMedicamentColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button supprimerButton;
    @FXML
    private TextField nom;
    @FXML
    private TextField id;
    @FXML
    private TextField quantite;
    @FXML
    private TableColumn<ReceiptItem, Integer> receiptIdColumn;
    @FXML
    private TableColumn<ReceiptItem, String> receiptNomColumn;

    @FXML
    private TableColumn<ReceiptItem, Double> receiptPrixColumn;
    @FXML
    private TableColumn<ReceiptItem, Integer> receiptQuantiteColumn;
    @FXML
    private TableColumn<ReceiptItem, Double> receiptTotalColumn;

    private Executor exec;

    @FXML
    private void initialize() {
        filteredData = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
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
        PrixColumn.setCellValueFactory(cellData -> cellData.getValue().prixmedicamentProperty().asObject());
        QuantiteEnStockMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteenstockmedicamentProperty().asObject());
        OrdonnanceMedicamentColumn.setCellValueFactory(cellData -> cellData.getValue().ordonnancemedicamentProperty());

        receiptIdColumn.setCellValueFactory(cellData -> cellData.getValue().idreceiptProperty().asObject());
        receiptNomColumn.setCellValueFactory(cellData -> cellData.getValue().nomreceiptProperty());
        receiptPrixColumn.setCellValueFactory(cellData -> cellData.getValue().prixreceiptProperty().asObject());
        receiptQuantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantitereceiptProperty().asObject());
        receiptTotalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        totalLabel.setText("Total: Rs0.00");
        try {
            ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
            filteredData = new FilteredList<>(allMedicaments, p -> true);
            medicamentTable.setItems(filteredData);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception according to your application's logic
        }

        // Add a listener to the search text field
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medicament -> {
                // If filter text is empty, display all medicaments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert the filter text to lowercase for case-insensitive matching
                String lowerCaseFilter = newValue.toLowerCase();

                // Check if any of the medicament attributes contain the filter text
                if (medicament.getnommedicament().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches nomMedicament
                }
                // You can add more conditions to filter by other attributes if needed

                return false; // Does not match
            });
        });
        // Add listener to TableView selection model
        medicamentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Populate text fields with selected item's values
                Medicament selectedMedicament = (Medicament) medicamentTable.getSelectionModel().getSelectedItem();
                if (selectedMedicament != null) {
                    // Populate text fields with selected item's values
                    nom.setText(selectedMedicament.getnommedicament());
                    id.setText(String.valueOf(selectedMedicament.getidmedicament()));

                    // Set focus on the quantite text field after a short delay
                    Platform.runLater(() -> quantite.requestFocus());

                    // Populate other text fields accordingly
                }
            }
        });

        // Add event handler for Enter key press on text fields
        nom.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Check if all required text fields have values
                if (areAllFieldsFilled()) {
                    // Perform action (e.g., add item)
                    ajouterButtonClicked();
                } else {
                    // Display a message indicating that values are missing
                    error.setText("Please fill in all fields before adding.");
                }
            }
        });

        id.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Check if all required text fields have values
                if (areAllFieldsFilled()) {
                    // Perform action (e.g., add item)
                    ajouterButtonClicked();
                } else {
                    // Display a message indicating that values are missing
                    error.setText("Please fill in all fields before adding.");
                }
            }
        });

        quantite.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Check if all required text fields have values
                if (areAllFieldsFilled()) {
                    // Perform action (e.g., add item)
                    ajouterButtonClicked();
                } else {
                    // Display a message indicating that values are missing
                    error.setText("Please fill in all fields before adding.");
                }
            }
        });
        // Set cell factory for receiptTotalColumn to format numbers with two decimal places
        receiptTotalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            @Override
            public String toString(Double value) {
                DecimalFormat df = new DecimalFormat("#.00");
                return df.format(value);
            }
        }));

        ajouterButton.setOnAction(event -> {
            // Retrieve values from the TextFields
            String nomValue = nom.getText();
            int idValue;

            int quantiteValue;

            try {
                idValue = Integer.parseInt(id.getText());
                quantiteValue = Integer.parseInt(quantite.getText());

                // Retrieve the selected medicament from the medicamentTable
                Medicament selectedMedicament = (Medicament) medicamentTable.getSelectionModel().getSelectedItem();
                if (selectedMedicament != null) {
                    int quantiteEnStock = selectedMedicament.getquantiteenstockmedicament();

                    // Check if the quantity requested exceeds the quantity in stock
                    if (quantiteValue > quantiteEnStock) {
                        error.setText("We don't have enough medicament in stock.");
                        return; // Exit the method without adding the item to the receiptTable
                    }

                    double prixValue = selectedMedicament.getprixmedicament(); // Ensure price is retrieved correctly

                    // Calculate the total by multiplying quantity with price
                    double total = quantiteValue * prixValue;

                    // Create a new ReceiptItem object with the retrieved values
                    ReceiptItem newReceiptItem = new ReceiptItem(idValue, nomValue,prixValue, quantiteValue, prixValue);

                    // Add the new ReceiptItem object to the TableView's data source
                    receiptTable.getItems().add(newReceiptItem);

                    // Optionally, you can clear the TextFields after adding the ReceiptItem
                    clearTextFields();

                    // Update the total label
                    updateTotalLabel();
                } else {
                    error.setText("Please select a medicament.");
                }
            } catch (NumberFormatException e) {
                error.setText("Invalid character in quantity field");
            }
        });



        // Assuming you have a button named supprimerButton
        supprimerButton.setOnAction(event -> {
            // Get the selected item from the receiptTable
            ReceiptItem selectedItem = receiptTable.getSelectionModel().getSelectedItem();

            // Check if an item is selected
            if (selectedItem != null) {
                // Remove the selected item from the table
                receiptTable.getItems().remove(selectedItem);
            } else {
                // If no item is selected, you can display a message or handle it as per your application's logic
                System.out.println("Please select an item to remove.");
            }
        });

    }

    // Method to check if all required text fields have values
    private boolean areAllFieldsFilled() {
        return !nom.getText().isEmpty() && !id.getText().isEmpty() && !quantite.getText().isEmpty();
    }
    // Method to clear all the TextFields
    private void clearTextFields() {
        nom.clear();
        id.clear();
        quantite.clear();
    }


    private void insertDataIntoVenteTable(String receiptId, double total) {

        try {
            // Establish a connection to the database
            Connection conn = DBUtil.dbConnect();

            // Prepare the SQL INSERT statement for the initial row
            String initialInsertSQL = "INSERT INTO receipt (receipt_id,total, status, vendeur) VALUES (?, ?, ?, ?)";
            PreparedStatement initialPreparedStatement = conn.prepareStatement(initialInsertSQL);

            // Set the values for the parameters in the prepared statement for the initial row
            initialPreparedStatement.setString(1, receiptId); // Set the receipt ID for the initial row
            initialPreparedStatement.setDouble(2, total);
            initialPreparedStatement.setString(3, "en cours"); // Set the status for the initial row
            initialPreparedStatement.setString(4, username);


            // Execute the SQL INSERT statement for the initial row
            initialPreparedStatement.executeUpdate();

            // Prepare the SQL INSERT statement
            String insertSQL = "INSERT INTO receipt_items (receipt_id, nom, idmedicament,prix, quantite, total, status) VALUES (?, ?,?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

            // Retrieve data from each row of the receiptTable
            for (ReceiptItem item : receiptTable.getItems()) {
                // Set the values for the parameters in the prepared statement
                preparedStatement.setString(1, receiptId); // Set the receipt ID for all items in the receipt
                preparedStatement.setString(2, item.getnomreceipt());
                preparedStatement.setInt(3, item.getidreceipt());
                preparedStatement.setDouble(4, item.getprixreceipt());
                preparedStatement.setInt(5, item.getquantitereceipt());
                preparedStatement.setDouble(6, item.getTotal());
                preparedStatement.setString(7, "en cours");

                // Execute the SQL INSERT statement
                preparedStatement.executeUpdate();
            }

            // Other code...
        } catch (SQLException | ClassNotFoundException e) {
            // Handle exceptions
            e.printStackTrace();
            // Display an error message or perform error handling based on your application's requirements
            System.out.println("Error inserting data into Vente table: " + e.getMessage());
        }
    }

    @FXML
    private void imprimerButtonClicked(ActionEvent event) {
        try {
            generateReceiptPDF();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IO exception
        }
        String receiptId = generateUniqueReceiptId();
        double total = calculateTotal(); // Assuming you have a method to calculate the total
        insertDataIntoVenteTable(receiptId, total);
        // Call the method to generate the PDF receipt
        // Clear the items in the receiptTable
        receiptTable.getItems().clear();

        totalLabel.setText("Total: Rs0.00");

    }

    private String generateUniqueReceiptId() {
        // Generate a unique receipt ID using a combination of a static prefix and a timestamp
        String prefix = "RECEIPT_";
        String timestamp = String.valueOf(System.currentTimeMillis()); // Get current timestamp
        return prefix + timestamp;
    }




    @FXML
    private void populateMedicaments(ObservableList<Medicament> medicamentData) throws ClassNotFoundException {
        // Set items to the utilisateurTable
        medicamentTable.setItems(medicamentData);
    }

    @FXML
    private Label totalLabel;

    // Method to update the total label
    private void updateTotalLabel() {
        double total = 0.0;

        // Iterate over the items in the receipt table
        for (ReceiptItem item : receiptTable.getItems()) {
            total += item.getTotal(); // Assuming ReceiptItem has a method to retrieve the total value
        }

        // Format the total value with two decimal places and update the total label
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedTotal = "Total: Rs" + df.format(total);
        totalLabel.setText(formattedTotal);
    }

    private void generateReceiptPDF() throws IOException {
        // Create a new PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new page content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Set font and font size for the receipt ID
        PDFont font = PDType1Font.HELVETICA_BOLD;
        float fontSize = 24; // Adjust the font size as needed

        // Generate the receipt ID
        String receiptId = generateUniqueReceiptId();

        // Calculate the width of the receipt ID text
        float textWidth = font.getStringWidth(receiptId) / 1000 * fontSize;

        // Calculate the X coordinate for centering the text
        float startX = (page.getMediaBox().getWidth() - textWidth) / 2;

        // Set the Y coordinate for the receipt ID text
        float startY = page.getMediaBox().getHeight() - 50; // Adjust the Y coordinate as needed

        // Begin the text rendering for the receipt ID
        contentStream.beginText();

        // Set the font and font size for the receipt ID
        contentStream.setFont(font, fontSize);

        // Set the text position for the receipt ID
        contentStream.newLineAtOffset(startX, startY);

        // Render the receipt ID text
        contentStream.showText(receiptId);

        // End the text rendering for the receipt ID
        contentStream.endText();

        // Set font and font size
        contentStream.setFont(PDType1Font.HELVETICA, 12);

        float startX1 = 50;
        float columnWidth = 200; // Width for each column
        float lineHeight = 25; // Height for each line
        float lineWidth = 0.5f; // Line width
        float lineThickness = 1;


// Iterate over the items in the receipt table and write them to the PDF
        for (ReceiptItem item : receiptTable.getItems()) {
            // Ensure startY is adjusted for the next line
            startY -= 2 * lineHeight;

            // Check if there's enough space on the page for another line
            if (startY < 50) {
                // If not enough space, create a new page
                contentStream.close(); // Close the current content stream
                page = new PDPage(); // Create a new page
                document.addPage(page); // Add the new page to the document
                contentStream = new PDPageContentStream(document, page); // Create a new content stream for the new page
                startY = page.getMediaBox().getHeight() - 50; // Reset startY for the new page
            }


            // Write the content of each column
            contentStream.beginText();
            contentStream.newLineAtOffset(startX1, startY);
            contentStream.showText("ID: " + item.getidreceipt());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Nom: " + item.getnomreceipt());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Quantite: " + item.getquantitereceipt());
            contentStream.newLineAtOffset(-columnWidth, -lineHeight); // Move to the next line
            contentStream.showText("Prix: Rs" + item.getprixreceipt());
            // Format the total value with two decimal places
            DecimalFormat df = new DecimalFormat("#.00");
            String formattedTotal = "Rs" + df.format(item.getTotal());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Total: " + formattedTotal);

            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.endText();

            // Draw horizontal line between rows
            contentStream.setLineWidth(lineWidth);
            contentStream.moveTo(startX1, startY - 30);
            contentStream.lineTo(startX1 + 5 * columnWidth, startY - 32);
            contentStream.stroke();
        }

        // Add the total at the bottom right of the page
        String totalText = totalLabel.getText(); // Assuming totalLabel is a JavaFX Label
        float totalTextWidth = PDType1Font.HELVETICA.getStringWidth(totalText) / 1000 * 12; // Calculate the width of the text
        float totalTextHeight = 12; // Assuming font size is 12
        float totalTextX = page.getMediaBox().getWidth() - totalTextWidth - 50; // Adjust the X coordinate as needed
        float totalTextY = 50; // Adjust the Y coordinate as needed
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(totalTextX, totalTextY);
        contentStream.showText(totalText);
        contentStream.endText();

        // Close the content stream
        contentStream.close();

        // Save the PDF document to a file
        File file = new File("receipt.pdf");
        document.save(file);
        document.close();

        openPDFFile(file);
    }

    // Method to open the saved PDF file
    private void openPDFFile(File file) {
        if (Desktop.isDesktopSupported()) { // Check if Desktop is supported
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) { // Check if the file exists
                try {
                    desktop.open(file); // Open the file with the default PDF viewer
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception
                }
            } else {
                // Handle if the file doesn't exist
                System.out.println("File not found.");
            }
        } else {
            // Handle if Desktop is not supported
            System.out.println("Opening files is not supported on this platform.");
        }
    }

    private double calculateTotal() {
        double total = 0.0;

        // Iterate over the items in the receipt table
        for (ReceiptItem item : receiptTable.getItems()) {
            total += item.getTotal(); // Assuming ReceiptItem has a method to retrieve the total value
        }

        return total;
    }


    @FXML
    private void clearReceiptTable(ActionEvent event) {
        // Clear the items in the receiptTable
        receiptTable.getItems().clear();

        totalLabel.setText("Total: Rs0.00");
    }

    @FXML
    private void ajouterButtonClicked() {
        // Retrieve values from the TextFields
        String nomValue = nom.getText();
        int idValue;

        int quantiteValue;

        try {
            idValue = Integer.parseInt(id.getText());
            quantiteValue = Integer.parseInt(quantite.getText());

            // Retrieve the selected medicament from the medicamentTable
            Medicament selectedMedicament = (Medicament) medicamentTable.getSelectionModel().getSelectedItem();
            if (selectedMedicament != null) {
                int quantiteEnStock = selectedMedicament.getquantiteenstockmedicament();

                // Check if the quantity requested exceeds the quantity in stock
                if (quantiteValue > quantiteEnStock) {
                    error.setText("We don't have enough medicament in stock.");
                    return; // Exit the method without adding the item to the receiptTable
                }

                double prixValue = selectedMedicament.getprixmedicament(); // Ensure price is retrieved correctly

                // Calculate the total by multiplying quantity with price
                double total = quantiteValue * prixValue;

                // Create a new ReceiptItem object with the retrieved values
                ReceiptItem newReceiptItem = new ReceiptItem(idValue, nomValue, prixValue, quantiteValue, prixValue);

                // Add the new ReceiptItem object to the TableView's data source
                receiptTable.getItems().add(newReceiptItem);

                // Optionally, you can clear the TextFields after adding the ReceiptItem
                clearTextFields();

                // Update the total label
                updateTotalLabel();
            } else {
                error.setText("Please select a medicament.");
            }
        } catch (NumberFormatException e) {
            error.setText("Invalid character in quantity field");
        }
    }

}