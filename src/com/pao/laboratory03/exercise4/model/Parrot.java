package com.pao.laboratory03.exercise4.model;

public class Parrot extends Animal {

    private int knownWords;

    public Parrot(String name, int age, int knownWords) {
        super(name, age);
        this.knownWords = knownWords;
    }

    public int getKnownWords() {
        return knownWords;
    }

    @Override
    public String sound() {
        return "Squawk! (știe " + knownWords + " cuvinte)";
    }
}
