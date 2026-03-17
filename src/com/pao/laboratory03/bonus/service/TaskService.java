package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.enums.Status;
import com.pao.laboratory03.bonus.enums.Priority;
import com.pao.laboratory03.bonus.exception.DuplicateTaskException;
import com.pao.laboratory03.bonus.exception.InvalidTransitionException;
import com.pao.laboratory03.bonus.exception.TaskNotFoundException;
import com.pao.laboratory03.bonus.model.Task;

import java.util.*;

public class TaskService {

    private static TaskService instance;

    private Map<String, Task> tasksById = new HashMap<>();
    private Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private List<String> auditLog = new ArrayList<>();

    private int counter = 1;

    private TaskService() {}

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {

        String id = String.format("T%03d", counter++);

        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task duplicat");
        }

        Task task = new Task(id, title, priority);

        tasksById.put(id, task);

        tasksByPriority.putIfAbsent(priority, new ArrayList<>());
        tasksByPriority.get(priority).add(task);

        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");

        return task;
    }

    public void assignTask(String taskId, String assignee) {

        Task t = getTask(taskId);

        t.setAssignee(assignee);

        auditLog.add("[ASSIGN] " + taskId + " -> " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {

        Task t = getTask(taskId);

        if (!t.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(t.getStatus(), newStatus);
        }

        auditLog.add("[STATUS] " + taskId + ": " + t.getStatus() + " -> " + newStatus);

        t.setStatus(newStatus);
    }

    private Task getTask(String id) {
        Task t = tasksById.get(id);
        if (t == null) {
            throw new TaskNotFoundException("Task-ul '" + id + "' nu a fost găsit");
        }
        return t;
    }

    public List<Task> getTasksByPriority(Priority p) {
        return tasksByPriority.getOrDefault(p, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {

        Map<Status, Long> map = new HashMap<>();

        for (Task t : tasksById.values()) {
            map.put(t.getStatus(), map.getOrDefault(t.getStatus(), 0L) + 1);
        }

        return map;
    }

    public List<Task> getUnassignedTasks() {

        List<Task> list = new ArrayList<>();

        for (Task t : tasksById.values()) {
            if (t.getAssignee() == null) {
                list.add(t);
            }
        }

        return list;
    }

    public void printAuditLog() {
        auditLog.forEach(System.out::println);
    }

    public double getTotalUrgencyScore(int baseDays) {

        double sum = 0;

        for (Task t : tasksById.values()) {
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED) {
                sum += t.getPriority().calculateScore(baseDays);
            }
        }

        return sum;
    }
}
