package com.example.recyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnEnviar;
    TextView txt;
    private RecyclerView recyclerView;
    private List<Task> dataList = new ArrayList<>();
    ;
    private CustomAdapter customAdapter;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviar = findViewById(R.id.btnEnviar);
        txt = findViewById(R.id.textView);
        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        loadInformation();
        //deleteDatabase();

        //MAPEANDO COMPONENTE
        recyclerView = findViewById(R.id.rvDetalles);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (!dataList.isEmpty()) {
            customAdapter = new CustomAdapter(dataList, this, this);
            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });
    }

    //CARGAR LA INFORMACION EN EL ACTIVITY
    public void loadInformation() {
        String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_COMPLETED};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                int completedInt = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED));
                boolean completed = (completedInt != 0);

                Task task = new Task(id, title, description, 0, completed);
                dataList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void deleteDatabase() {
        File databaseFile = getApplicationContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if (databaseFile.exists()) {
            try {
                database.deleteDatabase(databaseFile);
                // La base de datos se ha eliminado exitosamente
            } catch (Exception e) {
                // Ocurrió un error al eliminar la base de datos
            }
        } else {
            // La base de datos no existe, no es necesario eliminarla
        }
    }
}