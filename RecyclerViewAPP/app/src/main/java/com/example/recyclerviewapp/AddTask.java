package com.example.recyclerviewapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class AddTask extends AppCompatActivity {

    Button btnBack;
    Button btnAdd;
    ImageButton btnImage;
    EditText txtTitle;
    EditText txtDescription;
    Bitmap bitmap;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //MAPEAR INFORMACION
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnDelete);
        btnImage = findViewById(R.id.btnImage);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);

        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();
                Random random = new Random();
                int randomNumber = random.nextInt(900000000) + 1000000000;
                Task newTask = new Task(0, title, description, randomNumber, false);

                if(!title.isEmpty()) {
                    //GUARDAR INFORMACION
                    if (bitmap != null) saveImage(bitmap, randomNumber);
                    insertTask(newTask);
                    //ABRIR ACTIVITY MAINACTIVITY
                    Intent intent = new Intent(AddTask.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddTask.this, "Se necesita al menos un título", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                btnImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Imágen cargada con éxito", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imágen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveImage(Bitmap bitmap, int name) {
        String filename = Integer.toString(name) + ".png"; // Cambia el nombre de archivo si deseas guardar múltiples imágenes
        File file = new File(getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Imágen guardada éxitosamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTask(Task newTask){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, newTask.title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, newTask.description);
        values.put(DatabaseHelper.COLUMN_IMAGE, newTask.image);
        values.put(DatabaseHelper.COLUMN_COMPLETED, newTask.completed);
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }
}