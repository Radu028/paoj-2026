package com.pao.laboratory03.bonus;

import java.util.*;

public class TaskService {
    private static TaskService instance;
    private Map<String, Task> tasksById = new HashMap<>();
    private Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private List<String> auditLog = new ArrayList<>();
    private int nextId = 1;

    private TaskService() {
        for (Priority p : Priority.values())
            tasksByPriority.put(p, new ArrayList<>());
    }

    public static TaskService getInstance() {
        if (instance == null) instance = new TaskService();
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        findById(taskId).setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " -> " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findById(taskId);
        if (!task.getStatus().canTransitionTo(newStatus))
            throw new InvalidTransitionException(task.getStatus(), newStatus);
        Status old = task.getStatus();
        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + old + " -> " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Status s : Status.values()) {
            long count = 0;
            for (Task t : tasksById.values())
                if (t.getStatus() == s) count++;
            summary.put(s, count);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();
        for (Task t : tasksById.values())
            if (t.getAssignee() == null) result.add(t);
        return result;
    }

    public void printAuditLog() {
        for (String entry : auditLog) System.out.println(entry);
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task t : tasksById.values())
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED)
                total += t.getPriority().calculateScore(baseDays);
        return total;
    }

    private Task findById(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost gasit");
        return task;
    }
}
