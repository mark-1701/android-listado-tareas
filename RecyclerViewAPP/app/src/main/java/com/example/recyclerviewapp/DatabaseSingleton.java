package com.example.recyclerviewapp;

public class DatabaseSingleton {

    private static DatabaseSingleton instance;

    private int selectedCard;

    private DatabaseSingleton() {}

    public static DatabaseSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    //METODOS PARA EL INDICE
    public int getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(int selectedCard) {
        this.selectedCard = selectedCard;
    }
}
