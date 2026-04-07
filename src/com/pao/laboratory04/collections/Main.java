package com.pao.laboratory04.collections;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // PARTEA A — HashMap (frecvența cuvintelor)
        String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
        HashMap<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            freq.put(w, freq.getOrDefault(w, 0) + 1);
        }

        System.out.println("=== PARTEA A: HashMap — frecvența cuvintelor ===");
        System.out.println("Frecvență: " + freq);
        System.out.println("Conține 'rust'? " + freq.containsKey("rust"));
        System.out.println("Chei: " + freq.keySet());
        System.out.println("Valori: " + freq.values());
        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // PARTEA B — TreeMap (sortare automată)
        TreeMap<String, Integer> sorted = new TreeMap<>(freq);
        System.out.println("\n=== PARTEA B: TreeMap — sortare automată ===");
        System.out.println("Sortat: " + sorted);
        System.out.println("Prima cheie: " + sorted.firstKey());
        System.out.println("Ultima cheie: " + sorted.lastKey());

        // PARTEA C — Map cu obiecte
        HashMap<String, List<String>> materii = new HashMap<>();
        materii.put("PAOJ", new ArrayList<>(Arrays.asList("Ana", "Mihai", "Ion")));
        materii.put("BD", new ArrayList<>(Arrays.asList("Ana", "Elena")));

        System.out.println("\n=== PARTEA C: Map cu obiecte ===");
        System.out.println("Studenți la PAOJ: " + materii.get("PAOJ"));
        materii.get("BD").add("George");
        System.out.println("Studenți la BD (actualizat): " + materii.get("BD"));
    }
}
