package com.example.recyclerviewapp;

public class Task {
    String title;
    String description;

    int image;

    Boolean completed;

    public Task(){

    }

    public Task(String title, String description, int image, Boolean completed) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", completed=" + completed +
                '}';
    }
}
