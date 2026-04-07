package com.pao.laboratory06.exercise2;

import java.util.*;

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

    public PersoanaJuridica() { this.tip = TipColaborator.SRL; }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void afiseaza() {
        System.out.printf("SRL: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override public String tipContract() { return "SRL"; }
}

class CIMColaborator extends PersoanaFizica {
    private double salariuBrutLunar;
    private boolean bonus;

    public CIMColaborator() { this.tip = TipColaborator.CIM; }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.salariuBrutLunar = in.nextDouble();
        String bonusStr = in.next();
        this.bonus = bonusStr.equals("DA");
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venit = salariuBrutLunar * 12 * 0.55;
        if (bonus) venit *= 1.10;
        return venit;
    }

    @Override
    public void afiseaza() {
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override public String tipContract() { return "CIM"; }
    @Override public boolean areBonus() { return bonus; }
}

class PFAColaborator extends PersoanaFizica {
    private static final double SALARIU_MINIM_ANUAL = 4050.0 * 12;
    private double venitLunar;
    private double cheltuieliLunare;

    public PFAColaborator() { this.tip = TipColaborator.PFA; }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNet;

        double cass;
        if (venitNet < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNet <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_ANUAL);
        }

        double cas;
        if (venitNet < 12 * SALARIU_MINIM_ANUAL) {
            cas = 0;
        } else if (venitNet <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }

    @Override public String tipContract() { return "PFA"; }
}

public class Main {
    public static void main(String[] args) {
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
        System.out.printf("\nColaborator cu venit net maxim: ");
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
            System.out.printf("%s: suma = %.2f lei, număr = %d\n", t, suma.get(t), numar.get(t));
        }
    }
}
