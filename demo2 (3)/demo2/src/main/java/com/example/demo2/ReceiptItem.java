package com.example.demo2;

import javafx.beans.property.*;

public class ReceiptItem {
    private IntegerProperty idreceipt;
    private StringProperty nomreceipt;
    private DoubleProperty prixreceipt;
    private IntegerProperty quantitereceipt;
    private DoubleProperty total;

    // Constructor
    public ReceiptItem(Integer idValue, String nomValue,Double prixValue, Integer quantiteValue, Double price) {
        this.idreceipt = new SimpleIntegerProperty(idValue);
        this.nomreceipt = new SimpleStringProperty(nomValue);
        this.prixreceipt = new SimpleDoubleProperty(prixValue);
        this.quantitereceipt = new SimpleIntegerProperty(quantiteValue);
        this.total = new SimpleDoubleProperty(quantiteValue * price);
    }


    // id_useraccounts
    public int getidreceipt() {
        return idreceipt.get();
    }

    public void setidreceipt(int idreceipts) {
        this.idreceipt.set(idreceipts);
    }

    public IntegerProperty idreceiptProperty() {
        return idreceipt;
    }

    // username
    public String getnomreceipt() {
        return nomreceipt.get();
    }

    public void setnomreceipt(String nomreceipt) {
        this.nomreceipt.set(nomreceipt);
    }

    public StringProperty nomreceiptProperty() {
        return nomreceipt;
    }
    public double getprixreceipt() {
        return prixreceipt.get();
    }

    public void setprixreceipt(double prixreceipt) {
        this.prixreceipt.set(prixreceipt);
    }

    public DoubleProperty prixreceiptProperty() {
        return prixreceipt;
    }

    // lastname
    public Integer getquantitereceipt() {
        return quantitereceipt.get();
    }

    public void setquantitereceipt(Integer quantitereceipt) {
        this.quantitereceipt.set(quantitereceipt);
    }

    public IntegerProperty quantitereceiptProperty() {
        return quantitereceipt;
    }
    // Getter and setter for total
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