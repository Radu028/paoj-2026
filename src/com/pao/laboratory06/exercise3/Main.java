package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

interface PlataOnline {
    void autentificare(String user, String parola);
    double consultareSold();
    boolean efectuarePlata(double suma);
}

interface PlataOnlineSMS extends PlataOnline {
    boolean trimiteSMS(String mesaj);
}

enum ConstanteFinanciare {
    TVA(0.19),
    SALARIU_MINIM(4050),
    COTA_IMPOZIT(0.10);

    private final double valoare;

    ConstanteFinanciare(double valoare) { this.valoare = valoare; }

    public double getValoare() { return valoare; }
}

abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected String telefon;

    Persoana(String nume, String prenume, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }

    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
}

class Angajat extends Persoana {
    protected double salariu;

    Angajat(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }
}

class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold = 10000;

    Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty())
            throw new IllegalArgumentException("User/parola invalide");
        System.out.println("Inginer " + nume + " autentificat.");
    }

    @Override
    public double consultareSold() { return sold; }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0 || suma > sold) return false;
        sold -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer other) { return this.nume.compareTo(other.nume); }

    @Override
    public String toString() { return "Inginer{" + nume + " " + prenume + ", salariu=" + salariu + "}"; }
}

class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private final List<String> smsTrimise = new ArrayList<>();
    private double sold = 50000;

    PersoanaJuridica(String nume, String prenume, String telefon) {
        super(nume, prenume, telefon);
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty())
            throw new IllegalArgumentException("User/parola invalide");
        System.out.println("PJ " + nume + " autentificata.");
    }

    @Override
    public double consultareSold() { return sold; }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0 || suma > sold) return false;
        sold -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) return false;
        if (telefon == null || telefon.isEmpty()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() { return smsTrimise; }

    @Override
    public String toString() { return "PJ{" + nume + " " + prenume + ", tel=" + telefon + "}"; }
}

class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer a, Inginer b) {
        return Double.compare(b.getSalariu(), a.getSalariu());
    }
}

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
            new Inginer("Popescu", "Ana", "0721000001", 8000),
            new Inginer("Ionescu", "Vlad", "0721000002", 9500),
            new Inginer("Georgescu", "Maria", "0721000003", 7000)
        };

        System.out.println("=== Sortare naturala (dupa nume) ===");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\n=== Sortare dupa salariu descrescator ===");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\n=== Acces prin referinta PlataOnline ===");
        PlataOnline po = ingineri[0];
        po.autentificare("user1", "pass1");
        System.out.println("Sold: " + po.consultareSold());
        System.out.println("Plata 500: " + po.efectuarePlata(500));
        System.out.println("Sold dupa plata: " + po.consultareSold());

        System.out.println("\n=== PersoanaJuridica cu SMS ===");
        PersoanaJuridica pj = new PersoanaJuridica("TechSRL", "SRL", "0721000010");
        PlataOnlineSMS smsRef = pj;
        smsRef.autentificare("admin", "secret");
        System.out.println("SMS trimis: " + smsRef.trimiteSMS("Plata procesata"));
        System.out.println("SMS trimis: " + smsRef.trimiteSMS("Confirmare"));
        System.out.println("SMS-uri: " + pj.getSmsTrimise());

        System.out.println("\n=== PJ fara telefon ===");
        PersoanaJuridica pjFaraTel = new PersoanaJuridica("NoPhone", "SRL", null);
        System.out.println("SMS fara telefon: " + pjFaraTel.trimiteSMS("Test"));

        System.out.println("\n=== Constante financiare ===");
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());

        System.out.println("\n=== Tratare erori ===");
        try {
            ingineri[0].autentificare(null, "pass");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }
    }
}
