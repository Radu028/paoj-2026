package com.pao.laboratory04.bonus;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TaskService service = TaskService.getInstance();

        // 1. Adaugă minim 5 task-uri
        System.out.println("=== Adăugare task-uri ===");
        Task t1 = service.addTask("Fix login bug", Priority.CRITICAL);
        System.out.println("Adăugat: " + t1);
        Task t2 = service.addTask("Add dark mode", Priority.LOW);
        System.out.println("Adăugat: " + t2);
        Task t3 = service.addTask("Update docs", Priority.MEDIUM);
        System.out.println("Adăugat: " + t3);
        Task t4 = service.addTask("Fix memory leak", Priority.HIGH);
        System.out.println("Adăugat: " + t4);
        Task t5 = service.addTask("Refactor DB layer", Priority.HIGH);
        System.out.println("Adăugat: " + t5);

        // 2. Asignează 3 task-uri
        System.out.println("\n=== Asignare ===");
        service.assignTask("T001", "Ana");
        System.out.println("T001 → Ana");
        service.assignTask("T003", "Mihai");
        System.out.println("T003 → Mihai");
        service.assignTask("T004", "Elena");
        System.out.println("T004 → Elena");

        // 3. Schimbă status-ul
        System.out.println("\n=== Schimbări status ===");
        service.changeStatus("T001", Status.IN_PROGRESS);
        System.out.println("T001: TODO → IN_PROGRESS ✓");
        service.changeStatus("T001", Status.DONE);
        System.out.println("T001: IN_PROGRESS → DONE ✓");
        service.changeStatus("T003", Status.IN_PROGRESS);
        System.out.println("T003: TODO → IN_PROGRESS ✓");

        try {
            service.changeStatus("T001", Status.TODO);
        } catch (InvalidTransitionException e) {
            System.out.println("T001: DONE → TODO → InvalidTransitionException: " + e.getMessage());
        }

        // 4. Task-uri HIGH
        System.out.println("\n=== Task-uri HIGH ===");
        for (Task t : service.getTasksByPriority(Priority.HIGH)) {
            System.out.println(t);
        }

        // 5. Sumar status
        System.out.println("\n=== Sumar status ===");
        Map<Status, Long> summary = service.getStatusSummary();
        for (Map.Entry<Status, Long> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 6. Task-uri neasignate
        System.out.println("\n=== Task-uri neasignate ===");
        for (Task t : service.getUnassignedTasks()) {
            System.out.println(t.getId() + ": " + t.getTitle());
        }

        // 7. Scor urgență
        System.out.println("\n=== Scor urgență (baseDays=5) ===");
        System.out.println("Total: " + service.getTotalUrgencyScore(5));

        // 8. Audit log
        System.out.println("\n=== Audit Log ===");
        service.printAuditLog();

        // 9-10. Excepții
        System.out.println("\n=== Excepții ===");
        try {
            service.changeStatus("T999", Status.DONE);
        } catch (TaskNotFoundException e) {
            System.out.println("TaskNotFoundException: " + e.getMessage());
        }
    }
}
