package com.pao.laboratory04.bonus;

import java.util.*;

public class TaskService {
    private Map<String, Task> tasksById;
    private Map<Priority, List<Task>> tasksByPriority;
    private List<String> auditLog;
    private int nextId;

    private TaskService() {
        this.tasksById = new LinkedHashMap<>();
        this.tasksByPriority = new HashMap<>();
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
        this.auditLog = new ArrayList<>();
        this.nextId = 1;
    }

    private static class Holder {
        private static final TaskService INSTANCE = new TaskService();
    }

    public static TaskService getInstance() {
        return Holder.INSTANCE;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul '" + id + "' există deja");
        }
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findTask(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findTask(taskId);
        if (!task.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(task.getStatus(), newStatus);
        }
        Status oldStatus = task.getStatus();
        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            summary.put(s, 0L);
        }
        for (Task t : tasksById.values()) {
            summary.merge(t.getStatus(), 1L, Long::sum);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();
        for (Task t : tasksById.values()) {
            if (t.getAssignee() == null) {
                result.add(t);
            }
        }
        return result;
    }

    public void printAuditLog() {
        for (String entry : auditLog) {
            System.out.println(entry);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task t : tasksById.values()) {
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED) {
                total += t.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }

    private Task findTask(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return task;
    }
}
