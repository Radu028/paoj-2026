package com.pao.laboratory06.exercise2;

import java.util.*;
import java.util.stream.Collectors;

enum TipColaborator { CIM, PFA, SRL }

interface IOperatiiCitireScriere {
    void citeste(Scanner in);
    void afiseaza();
    String tipContract();
    default boolean areBonus() { return false; }
}

abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected TipColaborator tip;

    public TipColaborator getTip() { return tip; }
    public abstract double calculeazaVenitNetAnual();
}

abstract class PersoanaFizica extends Colaborator {}
class PersoanaJuridica extends Colaborator {
    protected double venitLunar;
    protected double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
        this.tip = TipColaborator.SRL;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void afiseaza() {
        System.out.printf("SRL: %s %s, venit net anual: %.2f lei%n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() { return "SRL"; }
}

class CIMColaborator extends PersoanaFizica {
    private double salariuBrutLunar;
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.salariuBrutLunar = in.nextDouble();
        String bonusStr = in.next();
        this.bonus = bonusStr.equals("DA");
        this.tip = TipColaborator.CIM;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = salariuBrutLunar * 12 * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }

    @Override
    public void afiseaza() {
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei%n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() { return "CIM"; }
    @Override
    public boolean areBonus() { return bonus; }
}

class PFAColaborator extends PersoanaFizica {
    private double venitLunar;
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM = 4050;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
        this.tip = TipColaborator.PFA;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitLunar - cheltuieliLunare) * 12;
        double salariuMinimAnual = SALARIU_MINIM * 12;

        double impozit = venitNet * 0.10;

        double cass;
        if (venitNet < 6 * salariuMinimAnual) {
            cass = 0.10 * 6 * salariuMinimAnual;
        } else if (venitNet <= 72 * salariuMinimAnual) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * 72 * salariuMinimAnual;
        }

        double cas;
        if (venitNet < 12 * salariuMinimAnual) {
            cas = 0;
        } else if (venitNet <= 24 * salariuMinimAnual) {
            cas = 0.25 * 12 * salariuMinimAnual;
        } else {
            cas = 0.25 * 24 * salariuMinimAnual;
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei%n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() { return "PFA"; }
}

public class Main {
    public static void main(String[] args) {
        // Schelet original (vezi Readme.md pentru cerințe):
//        Scanner in = new Scanner(System.in);
//        int n = in.nextInt();
//        List<Colaborator> colaboratori = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            String tip = in.next();
//            Colaborator c = switch (tip) {
//                case "CIM" -> { CIMColaborator obj = new CIMColaborator(); obj.citeste(in); yield obj; }
//                case "PFA" -> { PFAColaborator obj = new PFAColaborator(); obj.citeste(in); yield obj; }
//                case "SRL" -> { SRLColaborator obj = new SRLColaborator(); obj.citeste(in); yield obj; }
//                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
//            };
//            colaboratori.add(c);
//        }
//        // Sortează și afișează pe tip, fiecare descrescător după venit net anual
//        // Colaborator cu venit net maxim
//        // Colaboratori persoane juridice (SRL)
//        // Sume și număr colaboratori pe tip

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> {
                    CIMColaborator obj = new CIMColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "PFA" -> {
                    PFAColaborator obj = new PFAColaborator();
                    obj.citeste(in);
                    yield obj;
                }
                case "SRL" -> {
                    PersoanaJuridica obj = new PersoanaJuridica();
                    obj.citeste(in);
                    yield obj;
                }
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaboratori.add(c);
        }

        for (TipColaborator tipColab : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(c -> c.getTip() == tipColab)
                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                    .forEach(Colaborator::afiseaza);
        }

        Colaborator max = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        System.out.printf("%nColaborator cu venit net maxim: ");
        if (max != null) max.afiseaza();

        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);

        System.out.println("\nSume și număr colaboratori pe tip:");
        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        var typesOfCollaborators = new HashSet<TipColaborator>();
        for (Colaborator c : colaboratori) {
            typesOfCollaborators.add(c.getTip());
        }
        for (TipColaborator t : typesOfCollaborators) {
            suma.put(t, 0.0);
            numar.put(t, 0);
        }
        for (Colaborator c : colaboratori) {
            TipColaborator t = c.getTip();
            suma.put(t, suma.get(t) + c.calculeazaVenitNetAnual());
            numar.put(t, numar.get(t) + 1);
        }
        for (TipColaborator t : TipColaborator.values()) {
            System.out.printf("%s: suma = %.2f lei, număr = %d%n", t, suma.get(t), numar.get(t));
        }
    }
}
