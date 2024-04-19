package com.example.demo2;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Fournisseur {
    private IntegerProperty idfournisseur;
    private StringProperty nomfournisseur;
    private StringProperty adressefournisseur;
    private StringProperty contactfournisseur;
    private StringProperty emailfournisseur;

    // Constructor
    public Fournisseur() {
        this.idfournisseur = new SimpleIntegerProperty();
        this.nomfournisseur = new SimpleStringProperty();
        this.adressefournisseur = new SimpleStringProperty();
        this.contactfournisseur = new SimpleStringProperty();
        this.emailfournisseur = new SimpleStringProperty();
    }

    // id_useraccounts
    public int getidfournisseur() {
        return idfournisseur.get();
    }

    public void setidfournisseur(int idfournisseurs) {
        this.idfournisseur.set(idfournisseurs);
    }

    public IntegerProperty idfournisseurProperty() {
        return idfournisseur;
    }

    // username
    public String getnomfournisseur() {
        return nomfournisseur.get();
    }

    public void setnomfournisseur(String nomfournisseur) {
        this.nomfournisseur.set(nomfournisseur);
    }

    public StringProperty nomfournisseurProperty() {
        return nomfournisseur;
    }

    // firstname
    public String getadressefournisseur() {
        return adressefournisseur.get();
    }

    public void setadressefournisseur(String adressefournisseur) {
        this.adressefournisseur.set(adressefournisseur);
    }

    public StringProperty adressefournisseurProperty() {
        return adressefournisseur;
    }

    // lastname
    public String getcontactfournisseur() {
        return contactfournisseur.get();
    }

    public void setcontactfournisseur(String contactfournisseur) {
        this.contactfournisseur.set(contactfournisseur);
    }

    public StringProperty contactfournisseurProperty() {
        return contactfournisseur;
    }

    // mdp_pharm
    public String getemailfournisseur() {
        return emailfournisseur.get();
    }

    public void setemailfournisseur(String emailfournisseur) {
        this.emailfournisseur.set(emailfournisseur);
    }

    public StringProperty emailfournisseurProperty() {
        return emailfournisseur;
    }

}