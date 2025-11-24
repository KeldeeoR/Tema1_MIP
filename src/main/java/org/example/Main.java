package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Produs> meniu = new ArrayList<>();

        meniu.add(new Mancare("Pizza Margherita", 45.0, 450));
        meniu.add(new Mancare("Paste Carbonara", 52.5, 400));
        meniu.add(new Bautura("Limonada", 15.0, 400));
        meniu.add(new Bautura("Apa Plata", 8.0, 500));

        System.out.println("−−− Meniu Restaurantul \"La Andrei\" −−−");
        for (Produs produs : meniu) {
            System.out.println("> " + produs + " − " + produs.detalii());
        }
        System.out.println("−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−");
    }
}
