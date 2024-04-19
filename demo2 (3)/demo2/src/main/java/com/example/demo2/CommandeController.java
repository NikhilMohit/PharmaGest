package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class CommandeController {



    @FXML
    private TableView<Commande> CommandeTable;

    @FXML
    private TableColumn<Commande, Integer> idmedicament;

    @FXML
    private TableColumn<Commande, String> nom;

    @FXML
    private TableColumn<Commande, String> fournisseur;

    @FXML
    private TableColumn<Commande, Integer> quantiteenstock;

    @FXML
    private TableColumn<Commande, Integer> quantitemincommande;

    @FXML
    private TableColumn<Commande, Integer> quantitemaxstock;

    @FXML
    private TableColumn<Commande, Integer> seuildecommande;

    @FXML
    private TableColumn<Commande, Double> prixunitaire;

    @FXML
    private Label labelquantiteenstock;

    @FXML
    private Label labelquantitemaxstock;

    @FXML
    private TextField quantitecommande;

    @FXML
    private Button recubutton;








    public void initialize() {
        // Initialize columns
        idmedicament.setCellValueFactory(cellData -> cellData.getValue().idmedicamentProperty().asObject());
        nom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        fournisseur.setCellValueFactory(cellData -> cellData.getValue().fournisseurProperty());
        quantiteenstock.setCellValueFactory(cellData -> cellData.getValue().quantiteenstockProperty().asObject());
        quantitemincommande.setCellValueFactory(cellData -> cellData.getValue().quantitemincommandeProperty().asObject());
        quantitemaxstock.setCellValueFactory(cellData -> cellData.getValue().quantitemaxstockProperty().asObject());
        seuildecommande.setCellValueFactory(cellData -> cellData.getValue().seuildecommandeProperty().asObject());
        prixunitaire.setCellValueFactory(cellData -> cellData.getValue().prixunitaireProperty().asObject());

        // Populate the table with data
        try {
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Add a listener to the TableView selection
        CommandeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update the labels and text field with the selected item's values
                labelquantiteenstock.setText(newValue.getQuantiteenstock() + " €");
                labelquantitemaxstock.setText(newValue.getQuantitemaxstock() + " €");
                quantitecommande.setText(String.valueOf(newValue.getQuantitemincommande()));

                // Augmenter la quantité recommandée de 20%
                int nouvelleQuantiteRecommandee = (int) (newValue.getQuantitemincommande() * 1.2);
                quantitecommande.setText(String.valueOf(nouvelleQuantiteRecommandee));
            }
        });
    }

    private void populateTable() throws SQLException, ClassNotFoundException {
        // Establish database connection using DBUtil
        Connection conn = DBUtil.dbConnect();

        if (conn != null) {
            try {
                // Fetch data from the database
                String query = "SELECT * FROM medicament WHERE quantiteenstock <= seuildecommande";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Populate data into ObservableList
                ObservableList<Commande> commandes = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Commande commande = new Commande(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("fournisseur"),
                            resultSet.getInt("quantiteenstock"),
                            resultSet.getInt("quantitemincommande"),
                            resultSet.getInt("quantitemaxstock"),
                            resultSet.getInt("seuildecommande"),
                            resultSet.getDouble("prixcommande")
                    );
                    commandes.add(commande);
                }

                // Set items to the table
                CommandeTable.setItems(commandes);

                // Close resources
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close the connection
                conn.close();
            }
        }
    }

    @FXML
    private void recubuttonclicked(ActionEvent event) {
        Commande selectedCommande = CommandeTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            try {
                generateReceiptPDF(selectedCommande);
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer l'erreur de génération du PDF
            }
        } else {
            // Gérer si aucun médicament n'est sélectionné
        }
    }

    private void generateReceiptPDF(Commande selectedCommande ) throws IOException {
        // Define the file name for the PDF document
        String fileName = "recu.pdf";

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
        for (Commande item : CommandeTable.getItems()) {
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

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("ID Recu: " + selectedCommande.getIdmedicament());
            contentStream.newLineAtOffset(columnWidth + -80, 0); // Décaler vers la droite pour aligner avec le Nom Fournisseur
            contentStream.showText("Nom Fournisseur: " + selectedCommande.getFournisseur());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText("Quantité Commandée: " + quantitecommande.getText());
            contentStream.newLineAtOffset(-2 * columnWidth + 210, -lineHeight); // Passer à la ligne suivante
            contentStream.showText("Prix Unitaire: " + selectedCommande.getPrixunitaire() + " €");
            contentStream.newLineAtOffset(columnWidth + 20, 0); // Décaler vers la droite pour aligner avec le Prix Total
            double prixTotal = selectedCommande.getPrixunitaire() * Integer.parseInt(quantitecommande.getText());
            contentStream.showText("Prix Total: " + prixTotal + " €");
            contentStream.endText();

// Dessiner une ligne horizontale entre les lignes
            contentStream.setLineWidth(lineWidth);
            contentStream.moveTo(startX1, startY - 30);
            contentStream.lineTo(startX1 + 5 * columnWidth, startY - 32);
            contentStream.stroke();




        }

        // Close the content stream
        contentStream.close();

        // Save the document
        document.save(fileName);

        // Open the PDF file if it exists
        File file = new File(fileName);
        if (file.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        } else {
            // Handle if the file doesn't exist
            System.out.println("Fichier introuvable.");
        }

    }
    private String generateUniqueReceiptId() {
        // Générer un identifiant unique à l'aide d'un préfixe statique et d'un timestamp
        String prefix = "RECEIPT_";
        String timestamp = String.valueOf(System.currentTimeMillis()); // Obtenir le timestamp actuel
        return prefix + timestamp;


    }

}