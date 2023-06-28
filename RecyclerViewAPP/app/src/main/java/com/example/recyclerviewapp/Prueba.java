package com.example.recyclerviewapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class Prueba extends RecyclerView.Adapter<Prueba.ViewHOlder> {

    private Context context;
    private MainActivity mainActivity;

    private List<Task> cardItemList;


    public Prueba(List<Task> lista, Context con, MainActivity main) {
        this.cardItemList = lista;
        this.context = con;
        this.mainActivity = main;
    }

    @NonNull
    @Override
    public Prueba.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new ViewHOlder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, int position) {
        // cargar la informacion

        String data = cardItemList.get(position).toString();
        Task cardItem = cardItemList.get(position);

        // Aquí puedes obtener las vistas dentro del CardView y establecer los datos
        TextView titleTextView = holder.cardView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = holder.cardView.findViewById(R.id.descriptionTextView);
        ImageView imageView = holder.cardView.findViewById(R.id.completed);

        titleTextView.setText("Título: " + cardItem.getTitle());
        descriptionTextView.setText("Descripción:\n" + cardItem.getDescription());

        if (cardItem.getCompleted()) {
            imageView.setImageResource(R.drawable.check); // Establece la imagen1 en la ImageView
        } else {
            imageView.setImageResource(R.drawable.x); // Establece la imagen2 en la ImageView
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EditTask.class);
                holder.itemView.getContext().startActivity(intent);
                TaskListSingleton.getInstance().setSelectedCard(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHOlder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
