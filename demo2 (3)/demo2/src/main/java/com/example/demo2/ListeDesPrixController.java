package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executor;

import static com.example.demo2.DashboardController.username;

public class ListeDesPrixController {

    @FXML
    private TableView<Medicament> medicamentTable;

    @FXML
    private TableColumn<Medicament, String> datemiseajour;

    @FXML
    private TableColumn<Medicament, Double> prixunitaireachat;

    @FXML
    private TableColumn<Medicament, Double> prixvente;

    @FXML
    private TableColumn<Medicament, String> fournisseur;

    @FXML
    private TableColumn<Medicament, Integer> id;

    @FXML
    private TableColumn<Medicament, String> nom;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField BarDeRecherche;

    // Add your own executor for multithreading if needed
    private Executor exec;

    // Data to store all the Medicaments
    private ObservableList<Medicament> allMedicaments;

    // Initializing the controller class.
    // This method is automatically called after the fxml file has been loaded.
    @FXML
    private void initialize() {
        datemiseajour.setCellValueFactory(cellData -> cellData.getValue().datemiseajourmedicamentProperty());
        prixunitaireachat.setCellValueFactory(cellData -> cellData.getValue().prixunitaireachatmedicamentProperty().asObject());
        prixvente.setCellValueFactory(cellData -> cellData.getValue().prixventemedicamentProperty().asObject());
        fournisseur.setCellValueFactory(cellData -> cellData.getValue().fournisseurmedicamentProperty());
        id.setCellValueFactory(cellData -> cellData.getValue().idmedicamentProperty().asObject());
        nom.setCellValueFactory(cellData -> cellData.getValue().nommedicamentProperty());

        try {
            allMedicaments = MedicamentDAO.searchAllMedicaments();
            populateMedicaments(allMedicaments);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        setupSearchListener();
    }

    private void populateMedicaments(ObservableList<Medicament> medicamentData) {
        // Set items to the medicamentTable
        medicamentTable.setItems(medicamentData);
    }

    private void setupSearchListener() {
        // Wrap the ObservableList of items in a FilteredList
        FilteredList<Medicament> filteredList = new FilteredList<>(medicamentTable.getItems(), p -> true);

        // Add a change listener to the text property of the searchTextField
        BarDeRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(medicament -> {
                if (newValue == null || newValue.isEmpty()) {
                    // If the search text is empty, show all items
                    return true;
                }

                // Convert the search text to lowercase for case-insensitive search
                String searchText = newValue.toLowerCase();

                // Check if any of the properties of Medicament contain the search text
                return String.valueOf(medicament.getidmedicament()).toLowerCase().contains(searchText)
                        || String.valueOf(medicament.getprixunitaireachatmedicament()).toLowerCase().contains(searchText)
                        || String.valueOf(medicament.getprixventemedicament()).toLowerCase().contains(searchText)
                        || (medicament.getfournisseurmedicament() != null && medicament.getfournisseurmedicament().toLowerCase().contains(searchText))
                        || (medicament.getdatemiseajourmedicament() != null && medicament.getdatemiseajourmedicament().toLowerCase().contains(searchText))
                        || (medicament.getnommedicament() != null && medicament.getnommedicament().toLowerCase().contains(searchText));
            });
        });

        // Bind the filtered list to the TableView
        medicamentTable.setItems(filteredList);
    }



    @FXML
    private void handleImportExcelButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Process the selected Excel file
            importDataFromExcel(selectedFile);

            // Reload all data from the database and update the table
            try {
                ObservableList<Medicament> allMedicaments = MedicamentDAO.searchAllMedicaments();
                populateMedicaments(allMedicaments);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                // Handle appropriate exceptions
            }
        }
    }


    private void importDataFromExcel(File excelFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(excelFile);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            for (Row row : sheet) {
                // Assuming the data is separated by tabs, split the row's text by tabs
                String[] rowData = new String[row.getLastCellNum()];
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            rowData[i] = String.valueOf(cell.getNumericCellValue());
                        } else {
                            rowData[i] = cell.getStringCellValue();
                        }
                    } else {
                        rowData[i] = ""; // Or handle null values as needed
                    }
                }

                // Assuming the data is in specific columns, update the indices accordingly
                int id = (int) Double.parseDouble(rowData[0]); // Assuming the ID is in the first column

                double prixunitaireachat = Double.parseDouble(rowData[2]);
                double prixvente = Double.parseDouble(rowData[3]);

                // Assuming other data follows the same pattern

                // Create a new instance of Medicament with the constructor that accepts the necessary parameters
                Medicament medicament = new Medicament();
                medicament.setidmedicament(id);

                medicament.setprixunitaireachatmedicament(prixunitaireachat);
                medicament.setprixventemedicament(prixvente);

                // Set other fields as needed

                try {
                    // Call the DAO method that may throw a SQLException
                    MedicamentDAO.updateMedicamentListeDesPrix(medicament);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception locally if necessary
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle appropriate exceptions
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    }








