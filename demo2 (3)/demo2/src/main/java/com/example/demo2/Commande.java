package com.example.demo2;

import javafx.beans.property.*;

public class Commande {
    private final IntegerProperty idmedicament;
    private final StringProperty nom;
    private final StringProperty fournisseur;
    private final IntegerProperty quantiteenstock;
    private final IntegerProperty quantitemincommande;
    private final IntegerProperty quantitemaxstock;
    private final IntegerProperty seuildecommande;
    private final DoubleProperty prixunitaire;

    public Commande(int idmedicament, String nom, String fournisseur, int quantiteenstock,
                    int quantitemincommande, int quantitemaxstock, int seuildecommande, double prixunitaire) {
        this.idmedicament = new SimpleIntegerProperty(idmedicament);
        this.nom = new SimpleStringProperty(nom);
        this.fournisseur = new SimpleStringProperty(fournisseur);
        this.quantiteenstock = new SimpleIntegerProperty(quantiteenstock);
        this.quantitemincommande = new SimpleIntegerProperty(quantitemincommande);
        this.quantitemaxstock = new SimpleIntegerProperty(quantitemaxstock);
        this.seuildecommande = new SimpleIntegerProperty(seuildecommande);
        this.prixunitaire = new SimpleDoubleProperty(prixunitaire);
    }

    // Getter methods for properties
    public IntegerProperty idmedicamentProperty() {
        return idmedicament;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty fournisseurProperty() {
        return fournisseur;
    }

    public IntegerProperty quantiteenstockProperty() {
        return quantiteenstock;
    }

    public IntegerProperty quantitemincommandeProperty() {
        return quantitemincommande;
    }

    public IntegerProperty quantitemaxstockProperty() {
        return quantitemaxstock;
    }
    public IntegerProperty seuildecommandeProperty() {
        return seuildecommande;
    }

    public DoubleProperty prixunitaireProperty() {
        return prixunitaire;
    }

    // Getter methods for properties
    public int getIdmedicament() {
        return idmedicament.get();
    }

    public String getNom() {
        return nom.get();
    }

    public String getFournisseur() {
        return fournisseur.get();
    }

    public int getQuantiteenstock() {
        return quantiteenstock.get();
    }

    public int getQuantitemincommande() {
        return quantitemincommande.get();
    }

    public int getQuantitemaxstock() {
        return quantitemaxstock.get();
    }

    public int getSeuildecommande() {
        return seuildecommande.get();
    }

    public double getPrixunitaire() {
        return prixunitaire.get();
    }
}
