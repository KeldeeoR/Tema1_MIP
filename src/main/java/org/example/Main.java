package org.example;

public class Main {
    public static void main(String[] args) {

        // === ITERATIA 3: MENIU ===
        Meniu meniu = new Meniu();

        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL,
                new Mancare("Paste Arrabiata", 35, 300, true));

        meniu.adaugaProdus(Categorie.DESERT,
                new Mancare("Clatite cu ciocolata", 25, 200, true));

        meniu.adaugaProdus(Categorie.BAUTURI_RACORITOARE,
                new Bautura("Cola", 8, 330));

        // Pizza customizata cu Builder Pattern
        Pizza pizzaCustom = new Pizza.Builder(
                "Pizza House",
                50,
                450,
                false,   // nu e vegetariană
                "Subtire",
                "Rosii"
        )
                .adaugaTopping("Mozzarella")
                .adaugaTopping("Ciuperci")
                .build();

        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, pizzaCustom);

        // Afisări cerute de iteratia 3
        System.out.println("Vegetariene sortate: " + meniu.getProduseVegetarieneSortate());
        System.out.println("Pret mediu deserturi: " + meniu.getPretMediuDeserturi());
        System.out.println("Exista produse >100 RON? " + meniu.existaProdusePeste100());
        System.out.println("Caut 'Cola': " + meniu.cautaProdus("Cola"));


        // === ITERATIA 2: STRATEGII DISCOUNT ===

        // Strategie Happy Hour (20% reducere la bauturi)
        DiscountStrategy happyHour = (total, produse) -> {
            double reducere = 0;
            for (var entry : produse.entrySet()) {
                if (entry.getKey() instanceof Bautura) {
                    reducere += entry.getKey().getPret() * entry.getValue() * 0.20;
                }
            }
            return total - reducere;
        };

        // 10% reducere la toata comanda
        DiscountStrategy valentinesDay =
                (total, produse) -> total * 0.90;

        // 1+1: la fiecare mancare iei o bautura gratis
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
            return total - freeDrinks * 15; // limonada 15 lei
        };

        // Reducere fixa
        DiscountStrategy weekendSpecial = (total, produse) -> total - 5;


        // === ITERATIA 2: COMANDA ===
        Comanda comanda = new Comanda();

        comanda.adaugaProdus(new Mancare("Pizza Margherita", 45, 450, false), 2);
        comanda.adaugaProdus(new Bautura("Limonada", 15, 400), 1);


        comanda.setStrategieDiscount(valentinesDay);
        // comanda.setStrategieDiscount(happyHour);
        // comanda.setStrategieDiscount(pizzaPlusDrink);
        // comanda.setStrategieDiscount(weekendSpecial);

        System.out.println("Total de plata (cu discount): " + comanda.calculeazaTotal());
    }
}
