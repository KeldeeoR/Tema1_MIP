package org.example;

public class Mancare extends Produs {
    private double gramaj;

    public Mancare(String nume, double pret, double gramaj) {
        super(nume, pret);
        this.gramaj = gramaj;
    }

    @Override
    public String detalii() {
        return "Gramaj : " + gramaj + "g";
    }
}
