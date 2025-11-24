package org.example;

public class Main {
    public static void main(String[] args) {

        Comanda comanda = new Comanda();

        // Adaugam produse
        comanda.adaugaProdus(new Mancare("Pizza Margherita", 45, 450), 2);
        comanda.adaugaProdus(new Bautura("Limonada", 15, 400), 1);


        // Strategie Happy Hour
        DiscountStrategy happyHour = (total, produse) -> {
            double reducere = 0;
            for (var entry : produse.entrySet()) {
                if (entry.getKey() instanceof Bautura) {
                    reducere += entry.getKey().getPret() * entry.getValue() * 0.20;
                }
            }
            return total - reducere;
        };

        DiscountStrategy valentinesDay =
                (total, produse) -> total * 0.90;

        DiscountStrategy pizzaPlusDrink = (total, produse) -> {
            int pizzaCount = 0;
            int drinkCount = 0;

            for (var e : produse.entrySet()) {
                if (e.getKey() instanceof Mancare)
                    pizzaCount += e.getValue();
                else if (e.getKey() instanceof Bautura)
                    drinkCount += e.getValue();
            }

            int freeDrinks = Math.min(pizzaCount, drinkCount);
            return total - freeDrinks * 15; // 15 lei limonada
        };

        DiscountStrategy weekendSpecial = (total, produse) -> {
            return total - 5; // reducere fixa de 5 lei
        };


        comanda.setStrategieDiscount(valentinesDay);

        System.out.println("Total de plata: " + comanda.calculeazaTotal());
    }
}
