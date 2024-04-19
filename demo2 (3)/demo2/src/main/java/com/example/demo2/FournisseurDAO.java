package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FournisseurDAO {

    public static Fournisseur searchFournisseur(String fournId) throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM fournisseurs WHERE idfournisseur=" + fournId;

        try {
            ResultSet rsFournisseur = DBUtil.dbExecuteQuery(selectStmt);
            Fournisseur fournisseur = getFournisseurFromResultSet(rsFournisseur);
            return fournisseur;
        } catch (SQLException e) {
            System.out.println("While searching a fournisseur with " + fournId + " id, an error occurred: " + e);
            throw e;
        }
    }

    private static Fournisseur getFournisseurFromResultSet(ResultSet rs) throws SQLException {
        Fournisseur fournisseur = null;
        if (rs.next()) {
            fournisseur = new Fournisseur();
            fournisseur.setidfournisseur(rs.getInt("idfournisseur"));
            fournisseur.setnomfournisseur(rs.getString("nomfournisseur"));
            fournisseur.setadressefournisseur(rs.getString("adressefournisseur"));
            fournisseur.setcontactfournisseur(rs.getString("contactfournisseur"));
            fournisseur.setemailfournisseur(rs.getString("emailfournisseur"));
        }
        return fournisseur;
    }


    private static ObservableList<Fournisseur> getFournisseurList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();

        while (rs.next()) {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setidfournisseur(rs.getInt("idfournisseur"));
            fournisseur.setnomfournisseur(rs.getString("nomfournisseur"));
            fournisseur.setadressefournisseur(rs.getString("adressefournisseur"));
            fournisseur.setcontactfournisseur(rs.getString("contactfournisseur"));
            fournisseur.setemailfournisseur(rs.getString("emailfournisseur"));
            fournisseurList.add(fournisseur);
        }
        return fournisseurList;
    }

    public static void updateFournisseurNom(int fournId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE fournisseurs SET nomfournisseur = ? WHERE idfournisseur = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, fournId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateFournisseurAdresse(int fournId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE fournisseurs SET adressefournisseur = ? WHERE idfournisseur = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, fournId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateFournisseurContact(int fournId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE fournisseurs SET contactfournisseur = ? WHERE idfournisseur = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, fournId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateFournisseurMail(int fournId, String newValue) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE fournisseurs SET emailfournisseur = ? WHERE idfournisseur = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newValue);
                preparedStatement.setInt(2, fournId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }





    public static void insertFournisseur(String nomfournisseur, String adressefournisseur, String contactfournisseur, String emailfournisseur) throws SQLException, ClassNotFoundException {
        String updateStmt = "INSERT INTO fournisseurs " +
                "(idfournisseur, nomfournisseur, adressefournisseur, contactfournisseur, emailfournisseur) " +
                "VALUES " +
                "(nextval('sequence_utilisateur'), ?, ?, ?, ?)";

        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, nomfournisseur);
                preparedStatement.setString(2, adressefournisseur);
                preparedStatement.setString(3, contactfournisseur);
                preparedStatement.setString(4, emailfournisseur);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    public static void deletefournisseurwithid(String fournId) throws SQLException, ClassNotFoundException {
        String deleteStmt =
                "DELETE FROM fournisseurs\n" +
                        "WHERE idfournisseur = '" + fournId + "'";

        try {
            DBUtil.dbExecuteUpdate(deleteStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }


    public static ObservableList<Fournisseur> searchAllFournisseurs() throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM fournisseurs"; // Select all fournisseurs
        try {
            ResultSet rsFournisseurs = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<Fournisseur> fournisseurList = getFournisseurList(rsFournisseurs);
            return fournisseurList;
        } catch (SQLException e) {
            System.out.println("While searching for all fournisseurs, an error occurred: " + e);
            throw e;
        }
    }


}
