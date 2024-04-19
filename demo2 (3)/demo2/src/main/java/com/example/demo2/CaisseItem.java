package com.example.demo2;

import javafx.beans.property.*;

public class CaisseItem {
    private final StringProperty idReceipt;
    private final StringProperty vendeur;
    private final DoubleProperty total;

    public CaisseItem(String idReceipt,String vendeur, double total) {
        this.idReceipt = new SimpleStringProperty(idReceipt);
        this.vendeur = new SimpleStringProperty(vendeur);
        this.total = new SimpleDoubleProperty(total);
    }

    // Getter and setter methods for idReceipt
    public String getIdReceipt() {
        return idReceipt.get();
    }

    public void setIdReceipt(String idReceipt) {
        this.idReceipt.set(idReceipt);
    }

    public StringProperty idReceiptProperty() {
        return idReceipt;
    }

    public String getvendeur() {
        return vendeur.get();
    }

    public void setvendeur(String vendeur) {
        this.vendeur.set(vendeur);
    }

    public StringProperty vendeurProperty() {
        return vendeur;
    }

    // Getter and setter methods for total
    public double getTotal() {
        return total.get();
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
