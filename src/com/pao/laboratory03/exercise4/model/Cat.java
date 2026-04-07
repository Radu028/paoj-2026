package com.pao.laboratory03.exercise4.model;

public class Cat extends Animal {

    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public String sound() {
        return "Miau!";
    }
}
