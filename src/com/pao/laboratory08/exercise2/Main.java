package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while ((linie = fin.readLine()) != null) {
            if (linie.trim().isEmpty()) continue;
            String[] parts = linie.split(",");
            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();
            studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
        }
        fin.close();

        Scanner scanner = new Scanner(System.in);
        int prag = Integer.parseInt(scanner.nextLine().trim());

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        BufferedWriter fout = new BufferedWriter(new FileWriter("rezultate.txt"));
        for (Student s : filtrati) {
            fout.write(s.toString());
            fout.newLine();
        }
        fout.close();

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student s : filtrati) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
    }
}

