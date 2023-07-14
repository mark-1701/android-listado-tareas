package com.example.recyclerviewapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class EditTask extends AppCompatActivity {
    Button btnBack;
    Button btnDelete;
    Button btnEdit;
    ImageButton btnImage;
    EditText txtTitle;
    EditText txtDescription;
    CheckBox chkCompleted;
    Bitmap bitmap;
    int id;
    int newNameImage = -1;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //MAPEAR INFORMACION
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        btnImage = findViewById(R.id.btnImage);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        chkCompleted = findViewById(R.id.chkCompleted);
        id = databaseSingleton.getSelectedCard();

        //INSTANCIA SQLITE
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        loadInformation();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        //REGRESAR
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //ELIMINAR ELEMENTO
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(id);
                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //EDITAR CAMBIOS
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = txtTitle.getText().toString();
                String newDescription = txtDescription.getText().toString();
                Boolean newCompleted = chkCompleted.isChecked();

                if (newNameImage!=-1) {
                    saveImage(bitmap, newNameImage);
                }
                updateTask(newTitle, newDescription, newNameImage, newCompleted);

                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //CARGAR LA INFORMACION EN EL ACTIVITY
    private void loadInformation() {
        String[] projection = {DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_COMPLETED, DatabaseHelper.COLUMN_IMAGE};
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
                int completedInt = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED));
                boolean completed = (completedInt != 0);

                txtTitle.setText(title.toString());
                txtDescription.setText(description.toString());

                if (completed) {
                    chkCompleted.setChecked(true);
                } else {
                    chkCompleted.setChecked(false);
                }

                File file = new File(getFilesDir(), Integer.toString(image) + ".png");
                Uri imageUri = Uri.fromFile(file);
                if (file.exists()) {
                    btnImage.setImageURI(imageUri);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void updateTask(String newTitle, String newDescription, int newImage, Boolean newCompleted){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, newTitle);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, newDescription);
        if (newNameImage != -1) values.put(DatabaseHelper.COLUMN_IMAGE, newImage);
        values.put(DatabaseHelper.COLUMN_COMPLETED, newCompleted);
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs={Integer.toString(id)};
        database.update(databaseHelper.TABLE_NAME, values, selection, selectionArgs);
    }

    private void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona la aplicacion"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                btnImage.setImageBitmap(bitmap);
                Random random = new Random();
                newNameImage = random.nextInt(900000000) + 1000000000;
                Toast.makeText(this, "Imagen Cargada", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Imagen Guardada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(int id){
        String selection = DatabaseHelper.COLUMN_ID + "=?";
        String[] selectionArgs = {Integer.toString(id)};
        database.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
    }
}