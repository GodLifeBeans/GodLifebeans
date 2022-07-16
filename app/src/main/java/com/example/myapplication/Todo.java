package com.example.myapplication;

public class Todo {
    String content;
    boolean completed;

    public Todo(String content, boolean completed) {
        this.content = content;
        this.completed = completed;
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

