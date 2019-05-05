package com.example.mario.lacrim;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mario.lacrim.Entidades.Equinos;

import java.util.ArrayList;

public class Adaptador_seleccionar_equino extends RecyclerView.Adapter<Adaptador_seleccionar_equino.ViewHolder> {
    private ArrayList<Equinos> eq;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_seleccionar_equino(@NonNull ArrayList<Equinos> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.eq = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public class ViewHolder  extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nombre_eq;
        public TextView andar_eq;

        public ViewHolder(View v) {
            super(v);
            nombre_eq = itemView.findViewById(R.id.nombre_eq);
            andar_eq = itemView.findViewById(R.id.andar_eq);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }
    }

    public Adaptador_seleccionar_equino(ArrayList<Equinos> pan) {
        eq = pan;
    }

    public Adaptador_seleccionar_equino.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Adaptador_seleccionar_equino.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_seleccionar_equino, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre_eq.setText(((Equinos) eq.get(position)).getNombre_equino());
        holder.andar_eq.setText(((Equinos) eq.get(position)).getAndar_equino());
    }




    public int getItemCount() {
        return eq.size();
    }
}
