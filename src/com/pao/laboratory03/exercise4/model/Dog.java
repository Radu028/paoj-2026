package com.pao.laboratory03.exercise4.model;

public class Dog extends Animal {

    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public String sound() {
        return "Ham!";
    }
}
