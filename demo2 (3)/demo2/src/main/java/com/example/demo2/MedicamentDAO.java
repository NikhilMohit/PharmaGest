package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MedicamentDAO {

    public static Medicament searchMedicament(String medicamentId) throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM medicament WHERE id=" + medicamentId;

        try {
            ResultSet rsMedicament = DBUtil.dbExecuteQuery(selectStmt);
            Medicament medicament = getMedicamentFromResultSet(rsMedicament);
            return medicament;
        } catch (SQLException e) {
            System.out.println("While searching a medicament with " + medicamentId + " id, an error occurred: " + e);
            throw e;
        }
    }

    private static Medicament getMedicamentFromResultSet(ResultSet rs) throws SQLException {
        Medicament medicament = null;
        if (rs.next()) {
            medicament = new Medicament();
            medicament.setidmedicament(rs.getInt("id"));
            medicament.setnommedicament(rs.getString("nom"));
            medicament.setfournisseurmedicament(rs.getString("fournisseur"));
            medicament.setfamillemedicament(rs.getString("famille"));
            medicament.setformemedicament(rs.getString("forme"));
            medicament.setquantitemincommandemedicament(rs.getInt("quantitemincommande"));
            medicament.setquantitemaxstockmedicament(rs.getInt("quantitemaxstock"));
            medicament.setquantiteenstockmedicament(rs.getInt("quantiteenstock"));
            medicament.setordonnancemedicament(rs.getBoolean("ordonnance"));
            medicament.setprixmedicament(rs.getDouble("prixvente"));
            medicament.setprixunitaireachatmedicament(rs.getDouble("prixunitaireachat"));
            medicament.setprixventemedicament(rs.getDouble("prixvente"));
            medicament.setdatemiseajourmedicament(rs.getString("datemiseajour"));
        }
        return medicament;
    }


    private static ObservableList<Medicament> getMedicamentList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

        while (rs.next()) {
            Medicament medicament = new Medicament();
            medicament.setidmedicament(rs.getInt("id"));
            medicament.setnommedicament(rs.getString("nom"));
            medicament.setfournisseurmedicament(rs.getString("fournisseur"));
            medicament.setfamillemedicament(rs.getString("famille"));
            medicament.setformemedicament(rs.getString("forme"));
            medicament.setquantitemincommandemedicament(rs.getInt("quantitemincommande"));
            medicament.setquantitemaxstockmedicament(rs.getInt("quantitemaxstock"));
            medicament.setquantiteenstockmedicament(rs.getInt("quantiteenstock"));
            medicament.setordonnancemedicament(rs.getBoolean("ordonnance"));
            medicament.setprixmedicament(rs.getDouble("prixvente"));
            medicament.setprixunitaireachatmedicament(rs.getDouble("prixunitaireachat"));
            medicament.setprixventemedicament(rs.getDouble("prixvente"));
            medicament.setdatemiseajourmedicament(rs.getString("datemiseajour"));

            // Add the created Medicament object to the list
            medicamentList.add(medicament);
        }
        return medicamentList;
    }


    public static void updateMedicamentNom(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET nom = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentFournisseur(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET fournisseur = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentFamille(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET famille = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentForme(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET forme = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentQuantiteMinCommande(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET quantitemincommande = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentQuantiteMaxStock(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET quantitemaxstock = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentQuantiteEnStock(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET quantiteenstock = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateMedicamentOrdonnance(int medicamentId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET ordonnance = ? WHERE id = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, medicamentId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }



    public static void insertMedicament(String nommedicament, String fournisseurmedicament, String famillemedicament, String formemedicament, Integer quantitemincommande, Integer quantitemaxstock, Integer quantiteenstock, Boolean ordonnance) throws SQLException, ClassNotFoundException {

        String insertStmt = "INSERT INTO medicament " +
                "(nom, fournisseur, famille, forme, quantitemincommande, quantitemaxstock, quantiteenstock, ordonnance) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {


            try (PreparedStatement insertStatement = DBUtil.dbConnect().prepareStatement(insertStmt)) {
                insertStatement.setString(1, nommedicament);
                insertStatement.setString(2, fournisseurmedicament);
                insertStatement.setString(3, famillemedicament);
                insertStatement.setString(4, formemedicament);
                insertStatement.setInt(5, quantitemincommande);
                insertStatement.setInt(6, quantitemaxstock);
                insertStatement.setInt(7, quantiteenstock);
                insertStatement.setBoolean(8, ordonnance);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Medicament inserted successfully.");
                } else {
                    System.out.println("Failed to insert medicament.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while inserting medicament: " + e);
            throw e;
        }
    }

    public static boolean fournisseurExists(String fournisseurmedicament) throws SQLException, ClassNotFoundException {
        // SQL query to check if the fournisseur exists
        String query = "SELECT COUNT(*) FROM fournisseurs WHERE nomfournisseur = ?";

        // Try-with-resources to automatically close resources
        try (PreparedStatement statement = DBUtil.dbConnect().prepareStatement(query)) {
            statement.setString(1, fournisseurmedicament);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // If the result set has at least one row, the fournisseur exists
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }



    public static void deletemedicamentwithid(String medicamentId) throws SQLException, ClassNotFoundException {
        String deleteStmt =
                "DELETE FROM medicament\n" +
                        "WHERE id = '" + medicamentId + "'";

        try {
            DBUtil.dbExecuteUpdate(deleteStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }


    public static ObservableList<Medicament> searchAllMedicaments() throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM medicament"; // Select all medicament
        try {
            ResultSet rsMedicaments = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<Medicament> medicamentList = getMedicamentList(rsMedicaments);
            return medicamentList;
        } catch (SQLException e) {
            System.out.println("While searching for all medicaments, an error occurred: " + e);
            throw e;
        }
    }


    public static void updateMedicamentListeDesPrix(Medicament medicament) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE medicament SET " +
                "prixunitaireachat = ?, " +
                "prixvente = ? " +
                "WHERE id = ?";

        try (Connection connection = DBUtil.dbConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateStmt)) {

            preparedStatement.setDouble(1, medicament.getprixunitaireachatmedicament());
            preparedStatement.setDouble(2, medicament.getprixventemedicament());
            preparedStatement.setInt(3, medicament.getidmedicament()); // Assuming you have a method getId() in Medicament class

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Update successful. Rows updated: " + rowsUpdated);
            } else {
                System.out.println("No rows updated. Please check if the specified ID exists: " + medicament.getidmedicament());
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }


}
