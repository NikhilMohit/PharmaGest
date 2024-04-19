package com.example.demo2;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Utilisateur {
    private IntegerProperty id_useraccounts;
    private StringProperty username;
    private StringProperty firstname;
    private StringProperty lastname;
    private StringProperty mdp_pharm;
    private ObjectProperty<Timestamp> last_login; // Change to ObjectProperty<Timestamp>
    private StringProperty permission;

    // Constructor
    public Utilisateur() {
        this.id_useraccounts = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.firstname = new SimpleStringProperty();
        this.lastname = new SimpleStringProperty();
        this.mdp_pharm = new SimpleStringProperty();
        this.last_login = new SimpleObjectProperty<>(); // Initialize ObjectProperty
        this.permission = new SimpleStringProperty();
    }

    // id_useraccounts
    public int getIdUserAccounts() {
        return id_useraccounts.get();
    }

    public void setIdUserAccounts(int idUserAccounts) {
        this.id_useraccounts.set(idUserAccounts);
    }

    public IntegerProperty idUserAccountsProperty() {
        return id_useraccounts;
    }

    // username
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // firstname
    public String getFirstname() {
        return firstname.get();
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    // lastname
    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    // mdp_pharm
    public String getMdpPharm() {
        return mdp_pharm.get();
    }

    public void setMdpPharm(String mdpPharm) {
        this.mdp_pharm.set(mdpPharm);
    }

    public StringProperty mdpPharmProperty() {
        return mdp_pharm;
    }

    // last_login
    public Timestamp getLastLogin() {
        return last_login.get();
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.last_login.set(lastLogin);
    }

    public ObjectProperty<Timestamp> lastLoginProperty() {
        return last_login;
    }

    // permission
    public String getPermission() {
        return permission.get();
    }

    public void setPermission(String permission) {
        this.permission.set(permission);
    }

    public StringProperty permissionProperty() {
        return permission;
    }
}
