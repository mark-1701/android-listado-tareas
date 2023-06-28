package com.example.recyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Task> dataList;
    private CustomAdapter customAdapter;

    private Prueba prueba;

    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mapenado componenete
        recyclerView = findViewById(R.id.rvDetalles);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //asignando layout
        //LinearLayoutManager linearLayoutManger = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(linearLayoutManger);

        dataList = TaskListSingleton.getInstance().getTaskList();
        if (!dataList.isEmpty()) {
            //customAdapter = new CustomAdapter(dataList2, this, this);
            prueba = new Prueba(dataList, this, this);
            recyclerView.setAdapter(prueba);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        /*
        prueba = new Prueba(dataList, this, this);
        recyclerView.setAdapter(prueba);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });

    }

    public void setMensaje(String mensaje){
        TextView elemento = findViewById(R.id.textView);
        elemento.setText(mensaje);
    }
}