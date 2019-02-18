package com.example.mario.lacrim;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mario.lacrim.Entidades.Equinos;

import java.util.ArrayList;

public class Adaptador_lista extends RecyclerView.Adapter<Adaptador_lista.ViewHolder> {
    private ArrayList<Equinos> eq;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_lista(@NonNull ArrayList<Equinos> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.eq = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_nombre;
        public TextView tv_sexo;
        public TextView tv_andar;
        public TextView tv_color;

        public ViewHolder(View v) {
            super(v);
            tv_nombre = itemView.findViewById(R.id.tv_nombre_equino);
            tv_sexo = itemView.findViewById(R.id.tv_sexo_equino);
            tv_andar = itemView.findViewById(R.id.tv_andar_equino);
            tv_color = itemView.findViewById(R.id.tv_color_equino);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }


    }


    public Adaptador_lista(ArrayList<Equinos> pan) {
        eq = pan;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_nombre.setText(((Equinos) eq.get(position)).getNombre_equino());
        holder.tv_sexo.setText(((Equinos) eq.get(position)).getSexo_equino());
        holder.tv_andar.setText(((Equinos) eq.get(position)).getAndar_equino());
        holder.tv_color.setText(((Equinos) eq.get(position)).getColor_equino());
    }

    public int getItemCount() {
        return eq.size();
    }
}
