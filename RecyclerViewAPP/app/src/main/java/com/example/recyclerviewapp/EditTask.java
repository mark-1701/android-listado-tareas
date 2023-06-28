package com.example.recyclerviewapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private List<Task> dataList;

    Button btnBack;
    Button btnDelete;
    Button btnEdit;
    ImageButton btnImage;
    EditText txtTitle;
    EditText txtDescription;

    CheckBox chkCompleted;

    Bitmap bitmap;

    int newNameImage = -1;

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
        dataList = TaskListSingleton.getInstance().getTaskList();
        int position = TaskListSingleton.getInstance().getSelectedCard();

        //CARGAR INFORMACION
        txtTitle.setText(dataList.get(position).getTitle());
        txtDescription.setText(dataList.get(position).getDescription());
        boolean completed = dataList.get(position).getCompleted();
        if (completed) {
            chkCompleted.setChecked(true);
        } else {
            chkCompleted.setChecked(false);
        }

        int nameImage = dataList.get(position).getImage();

        File file = new File(getFilesDir(), Integer.toString(nameImage) + ".png");
        Uri imageUri = Uri.fromFile(file);
        btnImage.setImageURI(imageUri);

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
                Task taskToRemove = TaskListSingleton.getInstance().getTaskList().get(position);
                TaskListSingleton.getInstance().removeTask(taskToRemove);
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
                //IMAGEN
                if (newNameImage!=-1) {
                    saveImage(bitmap, newNameImage);
                    TaskListSingleton.getInstance().getTaskList().get(position).setImage(newNameImage);
                }
                TaskListSingleton.getInstance().getTaskList().get(position).setTitle(newTitle);
                TaskListSingleton.getInstance().getTaskList().get(position).setDescription(newDescription);
                TaskListSingleton.getInstance().getTaskList().get(position).setCompleted(newCompleted);

                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);

            }
        });
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
}