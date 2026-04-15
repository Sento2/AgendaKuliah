package com.kel4.agendakuliah.model;

public class Task {
    private int id;
    private String title;
    private String course;
    private String deadline;
    private int priority;
    private boolean done;
    private String description;
    private int userId;

    public Task() {}
    public Task(String title, String course, String deadline, int priority, String description, int userId) {
        this.title = title;
        this.course = course;
        this.deadline = deadline;
        this.priority = priority;
        this.description = description;
        this.userId = userId;
        this.done = false;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getCourse() { return course; }
    public void setCourse(String c) { this.course = c; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String d) { this.deadline = d; }
    public int getPriority() { return priority; }
    public void setPriority(int p) { this.priority = p; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public int getUserId() { return userId; }
    public void setUserId(int uid) { this.userId = uid; }

    public String getPriorityLabel() {
        switch (priority) {
            case 3: return "Tinggi";
            case 2: return "Sedang";
            default: return "Rendah";
        }
    }
}