package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UtilisateurDAO {

    public static Utilisateur searchUtilisateur(String userId) throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM useraccounts WHERE id_useraccounts=" + userId;

        try {
            ResultSet rsUtilisateur = DBUtil.dbExecuteQuery(selectStmt);
            Utilisateur utilisateur = getUtilisateurFromResultSet(rsUtilisateur);
            return utilisateur;
        } catch (SQLException e) {
            System.out.println("While searching a useraccounts with " + userId + " id, an error occurred: " + e);
            throw e;
        }
    }

    private static Utilisateur getUtilisateurFromResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = null;
        if (rs.next()) {
            utilisateur = new Utilisateur();
            utilisateur.setIdUserAccounts(rs.getInt("id_useraccounts"));
            utilisateur.setUsername(rs.getString("username"));
            utilisateur.setFirstname(rs.getString("firstname"));
            utilisateur.setLastname(rs.getString("lastname"));
            utilisateur.setMdpPharm(rs.getString("mdp_pharm"));
            utilisateur.setLastLogin(rs.getTimestamp("last_login"));
            utilisateur.setPermission(rs.getString("permission"));
        }
        return utilisateur;
    }

    public static ObservableList<Utilisateur> searchActiveUtilisateurs() throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT * FROM useraccounts WHERE status = true";
        try {
            ResultSet rsUtilisateurs = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<Utilisateur> utilisateurList = getUtilisateurList(rsUtilisateurs);
            return utilisateurList;
        } catch (SQLException e) {
            System.out.println("While searching for active utilisateurs, an error occurred: " + e);
            throw e;
        }
    }

    private static ObservableList<Utilisateur> getUtilisateurList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<Utilisateur> utilisateurList = FXCollections.observableArrayList();

        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setIdUserAccounts(rs.getInt("id_useraccounts"));
            utilisateur.setUsername(rs.getString("username"));
            utilisateur.setFirstname(rs.getString("firstname"));
            utilisateur.setLastname(rs.getString("lastname"));
            utilisateur.setMdpPharm(rs.getString("mdp_pharm"));
            utilisateur.setLastLogin(rs.getTimestamp("last_login"));
            utilisateur.setPermission(rs.getString("permission"));
            utilisateurList.add(utilisateur);
        }
        return utilisateurList;
    }

    public static void updateUtilisateurIdentifiant(int userId, String userEmail) throws SQLException, ClassNotFoundException {
        String updateStmt =
                "UPDATE useraccounts " +
                        "SET username = ? " +
                        "WHERE id_useraccounts = ?";

        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, userEmail);
                preparedStatement.setInt(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateUtilisateurNom(int userId, String newNom) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE useraccounts SET lastname = ? WHERE id_useraccounts = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newNom);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateUtilisateurPrenom(int userId, String newPrenom) throws SQLException, ClassNotFoundException {
        String updateStmt = "UPDATE useraccounts SET firstname = ? WHERE id_useraccounts = ?";
        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, newPrenom);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }


    public static void deactivateUserWithId(String userId) throws SQLException, ClassNotFoundException {
        String updateStmt =
                "UPDATE useraccounts\n" +
                        "SET status = false\n" +
                        "WHERE id_useraccounts = '" + userId + "'";

        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }


    public static void insertUtilisateur(String username, String firstname, String lastname, String mdpPharm) throws SQLException, ClassNotFoundException {
        String updateStmt = "INSERT INTO useraccounts " +
                "(id_useraccounts, username, firstname, lastname, mdp_pharm, last_login, permission) " +
                "VALUES " +
                "(nextval('sequence_utilisateur'), ?, ?, ?, ?, current_timestamp, 'User')";

        try {
            try (PreparedStatement preparedStatement = DBUtil.dbConnect().prepareStatement(updateStmt)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, firstname);
                preparedStatement.setString(3, lastname);
                preparedStatement.setString(4, mdpPharm);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
}
