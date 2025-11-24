package org.example;

import java.util.HashMap;
import java.util.Map;

public class Comanda {

    private final Map<Produs, Integer> produse = new HashMap<>();

    private static double TVA = 0.09;  // 9%

    private DiscountStrategy strategieDiscount = null;

    public void setStrategieDiscount(DiscountStrategy strategie) {
        this.strategieDiscount = strategie;
    }

    public void adaugaProdus(Produs produs, int cantitate) {
        produse.merge(produs, cantitate, Integer::sum);
    }

    public double calculeazaTotal() {
        double totalFaraTVA = 0;

        for (var entry : produse.entrySet()) {
            Produs produs = entry.getKey();
            int cantitate = entry.getValue();
            totalFaraTVA += produs.getPret() * cantitate;
        }

        // Aplicam discount-ul daca exista
        if (strategieDiscount != null) {
            totalFaraTVA = strategieDiscount.aplicaDiscount(totalFaraTVA, produse);
        }

        // Aplicam TVA
        return totalFaraTVA * (1 + TVA);
    }
}
