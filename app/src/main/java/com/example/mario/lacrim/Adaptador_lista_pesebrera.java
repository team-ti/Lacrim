package com.example.mario.lacrim;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Pesebrera;

import java.util.ArrayList;

public class Adaptador_lista_pesebrera extends RecyclerView.Adapter<Adaptador_lista_pesebrera.ViewHolder> {
    private ArrayList<Pesebrera> pes;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_lista_pesebrera(@NonNull ArrayList<Pesebrera> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.pes = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_nombre_pes;
        public TextView tv_encargado_pes;
        public TextView tv_ciudad_pes;
        public TextView tv_telefono_pes;

        public ViewHolder(View v) {
            super(v);
            tv_nombre_pes = itemView.findViewById(R.id.tv_nombre_pes);
            tv_encargado_pes = itemView.findViewById(R.id.tv_encargado_pes);
            tv_ciudad_pes = itemView.findViewById(R.id.tv_ciudad_pes);
            tv_telefono_pes = itemView.findViewById(R.id.tv_telefono_pes);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }


    }


    public Adaptador_lista_pesebrera(ArrayList<Pesebrera> pan) {
        pes = pan;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pesebrera, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_nombre_pes.setText(((Pesebrera) pes.get(position)).getNombre_pes());
        holder.tv_encargado_pes.setText(((Pesebrera) pes.get(position)).getEncargado_pes());
        holder.tv_ciudad_pes.setText(((Pesebrera) pes.get(position)).getCiudad_pes());
        holder.tv_telefono_pes.setText(((Pesebrera) pes.get(position)).getTelefono_pes());
    }

    public int getItemCount() {
        return pes.size();
    }
}
