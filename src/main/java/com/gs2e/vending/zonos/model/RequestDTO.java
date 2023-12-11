package com.gs2e.vending.zonos.model;

public class RequestDTO {
    private String numCompteur;
    private String univers;
    private double montant;

    public RequestDTO() {
    }

    public RequestDTO(String numCompteur, String univers, double montant) {
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

    @Override
    public String toString() {
        return "RequestDTO{" +
                "numCompteur='" + numCompteur + '\'' +
                ", univers='" + univers + '\'' +
                ", montant=" + montant +
                '}';
    }
}
