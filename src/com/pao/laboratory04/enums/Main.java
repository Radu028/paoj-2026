package com.pao.laboratory04.enums;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Toate prioritățile ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.getEmoji() + " " + p.name() + " (level=" + p.getLevel() + ", color=" + p.getColor() + ")");
        }

        System.out.println("\n=== Switch pe prioritate ===");
        Priority test = Priority.HIGH;
        switch (test) {
            case LOW -> System.out.println("Prioritate scăzută, fără urgență.");
            case MEDIUM -> System.out.println("Prioritate medie.");
            case HIGH -> System.out.println("⚠️ Atenție! Prioritate ridicată!");
            case CRITICAL -> System.out.println("🚨 CRITIC! Acțiune imediată!");
        }

        System.out.println("\n=== valueOf ===");
        Priority fromString = Priority.valueOf("HIGH");
        System.out.println("Priority.valueOf(\"HIGH\") = " + fromString);

        System.out.println("\n=== Comparare enum ===");
        System.out.println("HIGH == HIGH? " + (Priority.HIGH == Priority.HIGH));
        System.out.println("HIGH == LOW? " + (Priority.HIGH == Priority.LOW));

        System.out.println("\n=== name() și ordinal() ===");
        for (Priority p : Priority.values()) {
            System.out.println(p.name() + ": name=" + p.name() + ", ordinal=" + p.ordinal());
        }
    }
}
