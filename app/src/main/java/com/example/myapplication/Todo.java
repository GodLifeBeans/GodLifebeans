package com.example.myapplication;

public class Todo {
    String content;
    boolean completed;
    private String date ;

    public Todo(String date, String content, boolean completed) {
        this.date = date;
        this.content = content;
        this.completed = completed;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
