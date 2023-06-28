package com.example.recyclerviewapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaskListSingleton {

    private static TaskListSingleton instance;
    private List<Task> taskList;

    private int selectedCard;

    private TaskListSingleton() {
        taskList = new ArrayList<>();
    }

    public static TaskListSingleton getInstance() {
        if (instance == null) {
            instance = new TaskListSingleton();
        }
        return instance;
    }


    //METODOS PARA LA LISTA
    public void addTask(Task task) {
        taskList.add(task);
    }
    public void editTask(int position, Task task) {
        taskList.set(position, task);
    }

    public void removeTask(Task task) {
        taskList.remove(task);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    //METODOS PARA EL INDICE
    public int getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(int selectedCard) {
        this.selectedCard = selectedCard;
    }
}
