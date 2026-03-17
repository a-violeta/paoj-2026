package com.pao.laboratory03.bonus.model;

import com.pao.laboratory03.bonus.enums.Priority;
import com.pao.laboratory03.bonus.enums.Status;

public class Task {

    private String id;
    private String title;
    private Status status;
    private Priority priority;
    private String assignee;

    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;
    }

    public String getId() { return id; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public String getAssignee() { return assignee; }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "Task{id='" + id + "', title='" + title +
                "', priority=" + priority +
                ", status=" + status +
                ", assignee=" + assignee + "}";
    }
}