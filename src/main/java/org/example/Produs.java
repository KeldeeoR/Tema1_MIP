package org.example;

public abstract class Produs {
    protected String nume;
    protected double pret;

    public Produs(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public abstract String detalii();

    @Override
    public String toString() {
        return nume + " − " + pret + " RON";
    }
}
