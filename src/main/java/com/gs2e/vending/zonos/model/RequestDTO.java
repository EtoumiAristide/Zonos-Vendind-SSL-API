package com.gs2e.vending.zonos.model;

public class RequestDTO {
    private String userName;
    private String userPass;
    private String numCompteur;
    private String univers;
    private double montant;

    public RequestDTO() {
    }

    public RequestDTO(String userName, String userPass, String numCompteur, String univers, double montant) {
        this.userName = userName;
        this.userPass = userPass;
        this.numCompteur = numCompteur;
        this.univers = univers;
        this.montant = montant;
    }

    public String getNumCompteur() {
        return numCompteur;
    }

    public void setNumCompteur(String numCompteur) {
        this.numCompteur = numCompteur;
    }

    public String getUnivers() {
        return univers;
    }

    public void setUnivers(String univers) {
        this.univers = univers;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", numCompteur='" + numCompteur + '\'' +
                ", univers='" + univers + '\'' +
                ", montant=" + montant +
                '}';
    }
}
