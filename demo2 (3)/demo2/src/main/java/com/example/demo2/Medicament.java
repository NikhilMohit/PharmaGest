package com.example.demo2;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Medicament {
    private IntegerProperty idmedicament;
    private StringProperty nommedicament;
    private StringProperty fournisseurmedicament;
    private StringProperty famillemedicament;
    private StringProperty formemedicament;
    private IntegerProperty quantitemincommandemedicament;
    private IntegerProperty quantitemaxstockmedicament;
    private IntegerProperty quantiteenstockmedicament;
    private DoubleProperty prixmedicament;
    private BooleanProperty ordonnancemedicament;
    private DoubleProperty prixunitaireachatmedicament;

    private DoubleProperty prixventemedicament;

    private StringProperty datemiseajourmedicament;

    // Constructor
    public Medicament() {
        this.idmedicament = new SimpleIntegerProperty();
        this.nommedicament = new SimpleStringProperty();
        this.fournisseurmedicament = new SimpleStringProperty();
        this.famillemedicament = new SimpleStringProperty();
        this.formemedicament = new SimpleStringProperty();
        this.quantitemincommandemedicament = new SimpleIntegerProperty();
        this.quantitemaxstockmedicament = new SimpleIntegerProperty();
        this.quantiteenstockmedicament = new SimpleIntegerProperty();
        this.ordonnancemedicament = new SimpleBooleanProperty();
        this.prixmedicament = new SimpleDoubleProperty();
        this.prixunitaireachatmedicament = new SimpleDoubleProperty();
        this.prixventemedicament = new SimpleDoubleProperty();
        this.datemiseajourmedicament = new SimpleStringProperty();
    }

    // id_useraccounts
    public int getidmedicament() {
        return idmedicament.get();
    }

    public void setidmedicament(int idmedicaments) {
        this.idmedicament.set(idmedicaments);
    }

    public IntegerProperty idmedicamentProperty() {
        return idmedicament;
    }

    // username
    public String getnommedicament() {
        return nommedicament.get();
    }

    public void setnommedicament(String nommedicament) {
        this.nommedicament.set(nommedicament);
    }

    public StringProperty nommedicamentProperty() {
        return nommedicament;
    }

    // firstname
    public String getfournisseurmedicament() {
        return fournisseurmedicament.get();
    }

    public void setfournisseurmedicament(String fournisseurmedicament) { this.fournisseurmedicament.set(fournisseurmedicament);}

    public StringProperty fournisseurmedicamentProperty() {
        return fournisseurmedicament;
    }

    // lastname
    public String getfamillemedicament() {
        return famillemedicament.get();
    }

    public void setfamillemedicament(String famillemedicament) {
        this.famillemedicament.set(famillemedicament);
    }

    public StringProperty famillemedicamentProperty() {
        return famillemedicament;
    }

    // mdp_pharm
    public String getformemedicament() {
        return formemedicament.get();
    }

    public void setformemedicament(String formemedicament) {
        this.formemedicament.set(formemedicament);
    }

    public StringProperty formemedicamentProperty() {
        return formemedicament;
    }

    public int getquantitemincommandemedicament() {
        return quantitemincommandemedicament.get();
    }

    public void setquantitemincommandemedicament(int quantitemincommandemedicament) { this.quantitemincommandemedicament.set(quantitemincommandemedicament);}

    public IntegerProperty quantitemincommandemedicamentProperty() {
        return quantitemincommandemedicament;
    }

    public int getquantitemaxstockmedicament() {
        return quantitemaxstockmedicament.get();
    }

    public void setquantitemaxstockmedicament(int quantitemaxstockmedicament) { this.quantitemaxstockmedicament.set(quantitemaxstockmedicament);}

    public IntegerProperty quantitemaxstockmedicamentProperty() {
        return quantitemaxstockmedicament;
    }

    public int getquantiteenstockmedicament() {
        return quantiteenstockmedicament.get();
    }

    public void setquantiteenstockmedicament(int quantiteenstockmedicament) { this.quantiteenstockmedicament.set(quantiteenstockmedicament);}

    public IntegerProperty quantiteenstockmedicamentProperty() {
        return quantiteenstockmedicament;
    }

    public Boolean getordonnancemedicament() {return ordonnancemedicament.get();}

    public void setordonnancemedicament(Boolean ordonnancemedicament) { this.ordonnancemedicament.set(ordonnancemedicament);}

    public BooleanProperty ordonnancemedicamentProperty() {return ordonnancemedicament;}
    public Double getprixmedicament() {return prixmedicament.get();}

    public void setprixmedicament(Double prixmedicament) { this.prixmedicament.set(prixmedicament);}

    public DoubleProperty prixmedicamentProperty() {return prixmedicament;}

    public Double getprixunitaireachatmedicament() {
        return prixunitaireachatmedicament.get();
    }

    public void setprixunitaireachatmedicament(Double prixunitaireachatmedicament) {
        this.prixunitaireachatmedicament.set(prixunitaireachatmedicament);
    }

    public DoubleProperty prixunitaireachatmedicamentProperty() {
        return prixunitaireachatmedicament;
    }


    public Double getprixventemedicament() {
        return prixventemedicament.get();
    }

    public void setprixventemedicament(Double prixventemedicament) {
        this.prixventemedicament.set(prixventemedicament);
    }

    public DoubleProperty prixventemedicamentProperty() {
        return prixventemedicament;
    }


    public String getdatemiseajourmedicament() {
        return datemiseajourmedicament.get();
    }

    public void setdatemiseajourmedicament(String datemiseajourmedicament) {
        this.datemiseajourmedicament.set(datemiseajourmedicament);
    }

    public StringProperty datemiseajourmedicamentProperty() {
        return datemiseajourmedicament;
    }




}