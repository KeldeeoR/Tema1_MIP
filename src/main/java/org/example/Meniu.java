package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Meniu {

    // Mapa care ține produsele pe categorii
    private final Map<Categorie, List<Produs>> produsePeCategorii = new HashMap<>();

    // DTO simplu pentru serializare
    private static class ProductDTO {
        String type;
        String nume;
        double pret;
        Double gramaj;  // nullable
        Double volum;   // nullable
        List<String> toppinguri;  // nullable

        ProductDTO(String type, String nume, double pret) {
            this.type = type;
            this.nume = nume;
            this.pret = pret;
        }
    }

    /**
     * Exportă meniul curent într-un fișier JSON la path-ul specificat.
     * Returnează true dacă exportul a reușit, false altfel.
     */
    public boolean exportToJson(String path) {
        // Construim o mapă: categorie -> listă de produse DTO
        Map<String, List<ProductDTO>> serial = new LinkedHashMap<>();
        for (Categorie c : Categorie.values()) {
            serial.put(c.name(), new ArrayList<>());
        }

        // Parcurgem produsele și le adăugăm în DTO
        for (Map.Entry<Categorie, List<Produs>> entry : produsePeCategorii.entrySet()) {
            for (Produs p : entry.getValue()) {
                ProductDTO dto;

                // Dacă produsul este o Pizza
                if (p instanceof Pizza pizza) {
                    dto = new ProductDTO("Pizza", pizza.getNume(), pizza.getPret());
                    dto.toppinguri = pizza.getToppinguri();
                    dto.gramaj = null;  // Pizza nu are gramaj
                    dto.volum = null;   // Pizza nu are volum
                }
                // Dacă produsul este o Mancare
                else if (p instanceof Mancare m) {
                    dto = new ProductDTO("Mancare", m.getNume(), m.getPret());
                    dto.gramaj = m.getGramaj();
                    dto.volum = null;
                    dto.toppinguri = null;
                }
                // Dacă produsul este o Bautura
                else if (p instanceof Bautura b) {
                    dto = new ProductDTO("Bautura", b.getNume(), b.getPret());
                    dto.volum = b.getVolum();
                    dto.gramaj = null;
                    dto.toppinguri = null;
                }
                // Dacă este alt tip de produs (inclusiv Produs generic)
                else {
                    dto = new ProductDTO("Produs", p.getNume(), p.getPret());
                    dto.gramaj = null;
                    dto.volum = null;
                    dto.toppinguri = null;
                }

                // Adăugăm DTO-ul la categoria corespunzătoare
                serial.get(entry.getKey().name()).add(dto);
            }
        }

        // Creăm un obiect Gson pentru serializare (pentru a obține un JSON frumos)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fw = new FileWriter(path)) {
            // Scriem meniul serializat în fișier
            gson.toJson(serial, fw);
            return true;  // Exportul a reușit
        } catch (IOException e) {
            System.err.println("Eroare la export JSON: " + e.getMessage());
            return false;  // A apărut o eroare la salvarea fișierului
        }
    }

    // Metodă pentru adăugarea unui produs în Meniu
    public void adaugaProdus(Categorie categorie, Produs produs) {
        produsePeCategorii
                .computeIfAbsent(categorie, k -> new ArrayList<>())  // Creează o listă dacă nu există deja
                .add(produs);  // Adaugă produsul în categoria respectivă
    }
}
