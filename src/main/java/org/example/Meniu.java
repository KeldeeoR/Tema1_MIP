package org.example;

import java.util.*;

public class Meniu {

    private final Map<Categorie, List<Produs>> produsePeCategorii = new HashMap<>();

    public Meniu() {
        for (Categorie c : Categorie.values()) {
            produsePeCategorii.put(c, new ArrayList<>());
        }
    }

    public void adaugaProdus(Categorie categorie, Produs produs) {
        produsePeCategorii.get(categorie).add(produs);
    }

    public List<Produs> getProduseDinCategorie(Categorie categorie) {
        return Collections.unmodifiableList(produsePeCategorii.get(categorie));
    }

    public List<Produs> getProduseVegetarieneSortate() {
        return produsePeCategorii.values().stream()
                .flatMap(List::stream)
                .filter(p -> p instanceof Mancare && ((Mancare) p).isVegetarian())
                .sorted((a, b) -> a.getNume().compareToIgnoreCase(b.getNume()))
                .toList();
    }

    public double getPretMediuDeserturi() {
        return produsePeCategorii.get(Categorie.DESERT).stream()
                .mapToDouble(Produs::getPret)
                .average()
                .orElse(0);
    }

    public boolean existaProdusePeste100() {
        return produsePeCategorii.values().stream()
                .flatMap(List::stream)
                .anyMatch(p -> p.getPret() > 100);
    }

    public Optional<Produs> cautaProdus(String nume) {
        return produsePeCategorii.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getNume().equalsIgnoreCase(nume))
                .findFirst();
    }
}
