package org.example;

import java.util.Map;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {

        // 1. Încarcă configurația (config.json)
        AppConfig config = null;
        try {
            config = ConfigLoader.load("config.json");
            System.out.println("Config încărcat: restaurant = " + config.getRestaurantName() + ", TVA = " + config.getTva());
            Comanda.setTva(config.getTva()); // Setăm TVA-ul din config
        } catch (Exception e) {
            System.err.println("Eroare la încărcarea config.json: " + e.getMessage());
            // Dacă apare o eroare, continuăm cu TVA default
            Comanda.setTva(0.09);
        }

        // 2. Crează meniul și adaugă produse
        Meniu meniu = new Meniu();

        // Adăugăm 20 de produse în meniu
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Pizza Margherita", 45, 450, false));
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Pizza Quattro Stagioni", 50, 500, false));
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Lasagna Bolognese", 35, 350, false));
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Spaghetti Carbonara", 40, 400, false));
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Risotto cu ciuperci", 45, 450, true));
        meniu.adaugaProdus(Categorie.FEL_PRINCIPAL, new Mancare("Pasta Arrabiata", 38, 420, true));

        meniu.adaugaProdus(Categorie.BAUTURI_RACORITOARE, new Bautura("Cola", 8, 330));
        meniu.adaugaProdus(Categorie.BAUTURI_RACORITOARE, new Bautura("Fanta", 7.5, 330));
        meniu.adaugaProdus(Categorie.BAUTURI_RACORITOARE, new Bautura("Sprite", 7.5, 330));
        meniu.adaugaProdus(Categorie.BAUTURI_RACORITOARE, new Bautura("Apa minerala", 5, 500));

        meniu.adaugaProdus(Categorie.DESERT, new Mancare("Clatite cu ciocolata", 25, 200, true));
        meniu.adaugaProdus(Categorie.DESERT, new Mancare("Tiramisu", 28, 250, true));
        meniu.adaugaProdus(Categorie.DESERT, new Mancare("Cheesecake", 30, 220, true));
        meniu.adaugaProdus(Categorie.DESERT, new Mancare("Clatite cu gem", 20, 180, true));

        meniu.adaugaProdus(Categorie.BAUTURI_ALCOOLICE, new Bautura("Vin roșu", 40, 750));
        meniu.adaugaProdus(Categorie.BAUTURI_ALCOOLICE, new Bautura("Bere lager", 12, 500));
        meniu.adaugaProdus(Categorie.BAUTURI_ALCOOLICE, new Bautura("Gin", 35, 700));
        meniu.adaugaProdus(Categorie.BAUTURI_ALCOOLICE, new Bautura("Whisky", 60, 700));

        meniu.adaugaProdus(Categorie.APERITIVE, new Mancare("Bruschette cu roșii", 15, 150, true));
        meniu.adaugaProdus(Categorie.APERITIVE, new Mancare("Hummus cu pita", 18, 120, true));
        meniu.adaugaProdus(Categorie.APERITIVE, new Mancare("Aripioare de pui", 22, 180, false));
        meniu.adaugaProdus(Categorie.APERITIVE, new Mancare("Sufleu de brânză", 20, 160, true));

        // 3. Exportă meniul într-un fișier JSON
        boolean exportSuccess = meniu.exportToJson("meniu_export.json");
        if (exportSuccess) {
            System.out.println("Meniul a fost exportat cu succes în meniu_export.json");
        } else {
            System.out.println("Eroare la exportul meniului.");
        }

        // 4. Testăm funcționalitatea Comanda cu Discount

        // --- Exemplu 1: Comandă standard ---
        Comanda comanda1 = new Comanda();
        comanda1.adaugaProdus(new Mancare("Pizza Margherita", 45, 450, false), 2);
        comanda1.adaugaProdus(new Bautura("Cola", 8, 330), 1);

        // Aplicăm un discount de Valentine's Day
        DiscountStrategy valentinesDay = (total, produse) -> total * 0.90;
        comanda1.setStrategieDiscount(valentinesDay);

        // Afișăm bonul pentru comanda 1
        System.out.println("\n--- Bon pentru Comanda 1 ---");
        afiseazaBon(comanda1);

        System.out.println("Total de plată (cu discount) pentru comanda 1: " + String.format("%.2f", comanda1.calculeazaTotal()));

        // --- Exemplu 2: Comandă cu mai multe produse și discount ---
        Comanda comanda2 = new Comanda();
        comanda2.adaugaProdus(new Mancare("Spaghetti Carbonara", 40, 400, false), 3);
        comanda2.adaugaProdus(new Bautura("Fanta", 7.5, 330), 2);
        comanda2.adaugaProdus(new Mancare("Clatite cu ciocolata", 25, 200, true), 1);

        // Aplicăm un discount fix (Weekend Special)
        DiscountStrategy weekendSpecial = (total, produse) -> total - 5;  // reducere fixă de 5 lei
        comanda2.setStrategieDiscount(weekendSpecial);

        // Afișăm bonul pentru comanda 2
        System.out.println("\n--- Bon pentru Comanda 2 ---");
        afiseazaBon(comanda2);

        System.out.println("Total de plată (cu discount) pentru comanda 2: " + String.format("%.2f", comanda2.calculeazaTotal()));

        // --- Exemplu 3: Comandă cu produse mai scumpe și discount fix ---
        Comanda comanda3 = new Comanda();
        comanda3.adaugaProdus(new Mancare("Risotto cu ciuperci", 45, 450, true), 2);
        comanda3.adaugaProdus(new Bautura("Vin roșu", 40, 750), 1);

        // Aplicăm discountul Happy Hour (20% la băuturi)
        DiscountStrategy happyHour = (total, produse) -> {
            double reducere = 0;
            for (var entry : produse.entrySet()) {
                if (entry.getKey() instanceof Bautura) {
                    reducere += entry.getKey().getPret() * entry.getValue() * 0.20;
                }
            }
            return total - reducere;
        };
        comanda3.setStrategieDiscount(happyHour);

        // Afișăm bonul pentru comanda 3
        System.out.println("\n--- Bon pentru Comanda 3 ---");
        afiseazaBon(comanda3);

        System.out.println("Total de plată (cu discount) pentru comanda 3: " + String.format("%.2f", comanda3.calculeazaTotal()));
    }

    // Metodă pentru afișarea bonului cu produsele, cantitățile și prețurile
    public static void afiseazaBon(Comanda comanda) {
        System.out.println("---------------------------------------------------");
        System.out.println("Produse comandate:");

        // Iterăm prin produsele din comandă și le afișăm
        for (Map.Entry<Produs, Integer> entry : comanda.getProduse().entrySet()) {
            Produs produs = entry.getKey();
            int cantitate = entry.getValue();
            double pretUnitate = produs.getPret();
            double pretTotal = pretUnitate * cantitate;

            System.out.printf("%-20s %4d x %.2f RON = %.2f RON\n", produs.getNume(), cantitate, pretUnitate, pretTotal);
        }

        // Afișăm totalul final
        double totalFaraTVA = comanda.calculeazaTotal() / (1 + Comanda.getTva()); // Calculăm totalul fără TVA
        System.out.printf("\nTotal fara TVA: %.2f RON\n", totalFaraTVA);
        System.out.printf("Total cu TVA: %.2f RON\n", comanda.calculeazaTotal());
        System.out.println("---------------------------------------------------");
    }
}
