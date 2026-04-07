package com.pao.laboratory05.angajati;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajati =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine().trim();
                    System.out.print("Departament (nume): ");
                    String deptNume = scanner.nextLine().trim();
                    System.out.print("Departament (locatie): ");
                    String deptLoc = scanner.nextLine().trim();
                    System.out.print("Salariu: ");
                    double sal = Double.parseDouble(scanner.nextLine().trim());
                    service.addAngajat(new Angajat(nume, new Departament(deptNume, deptLoc), sal));
                    break;
                case "2":
                    System.out.println("--- Angajati dupa salariu (descrescator) ---");
                    service.listBySalary();
                    break;
                case "3":
                    System.out.print("Departament: ");
                    String dept = scanner.nextLine().trim();
                    System.out.println("--- Angajati din " + dept + " ---");
                    service.findByDepartament(dept);
                    break;
                case "0":
                    System.out.println("La revedere!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
}
