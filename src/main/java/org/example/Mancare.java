package org.example;

public final class Mancare extends Produs {
    private final double gramaj;

    public Mancare(String nume, double pret, double gramaj) {
        super(nume, pret);
        this.gramaj = gramaj;
    }

    @Override
    public String detalii() {
        return "Gramaj : " + gramaj + "g";
    }
}
