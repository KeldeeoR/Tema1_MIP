package org.example;

public class Bautura extends Produs {
    private double volum;

    public Bautura(String nume, double pret, double volum) {
        super(nume, pret);
        this.volum = volum;
    }

    @Override
    public String detalii() {
        return "Volum : " + volum + "ml";
    }
}
