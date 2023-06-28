package com.example.recyclerviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHOlder> {

    private List<String> dataList;
    private LinkedList<Task> dataList2;
    private int elementoSeleccionado;
    private Context context;
    private MainActivity mainActivity;

    public CustomAdapter(LinkedList<Task> lista, Context con, MainActivity main) {
        this.dataList2 = lista;
        this.context = con;
        this.mainActivity = main;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, int position) {
        // cargar la informacion

        String data = dataList2.get(position).toString();
        holder.textView.setText(data);

        /*
        if (elementoSeleccionado==position)
        {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.seleccion));
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.lista));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seleccionPrevia = elementoSeleccionado;
                elementoSeleccionado = position;
                String mensaje= "El elemento seleccionado es:" + dataList.get(position);
                mainActivity.setMensaje(mensaje);
                notifyItemChanged(seleccionPrevia);
                notifyItemChanged(elementoSeleccionado);
            }
        });
        */
    }


    @Override
    public int getItemCount() {
        return dataList2.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHOlder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvDetalle);
        }
    }
}
