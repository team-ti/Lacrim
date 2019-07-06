package com.example.mario.lacrim.Utilidades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mario.lacrim.Adaptador_lista;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.R;
import com.example.mario.lacrim.RecyclerViewOnItemClickListener;

import java.util.ArrayList;

public class Adaptador_lista_pesebrera_buscar extends RecyclerView.Adapter<Adaptador_lista_pesebrera_buscar.ViewHolder> {
    private ArrayList<Pesebrera> ps;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_lista_pesebrera_buscar(@NonNull ArrayList<Pesebrera> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.ps = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_nombre;
        public TextView tv_encargado;
        public TextView tv_ciudad;
        public TextView tv_telefono;

        public ViewHolder(View v) {
            super(v);
            tv_nombre = itemView.findViewById(R.id.tv_nombre_pes_buscar);
            tv_encargado = itemView.findViewById(R.id.tv_encargado_pes_buscar);
            tv_ciudad = itemView.findViewById(R.id.tv_ciudad_pes_buscar);
            tv_telefono = itemView.findViewById(R.id.tv_telefono_pes_buscar);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }


    }


    public Adaptador_lista_pesebrera_buscar(ArrayList<Pesebrera> pes) {
        ps = pes;
    }

    public Adaptador_lista_pesebrera_buscar.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Adaptador_lista_pesebrera_buscar.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pesebrera_buscar, parent, false));
    }

    public void onBindViewHolder(Adaptador_lista_pesebrera_buscar.ViewHolder holder, int position) {
        holder.tv_nombre.setText(((Pesebrera) ps.get(position)).getNombre_pes());
        holder.tv_encargado.setText(((Pesebrera) ps.get(position)).getEncargado_pes());
        holder.tv_ciudad.setText(((Pesebrera) ps.get(position)).getCiudad_pes());
        holder.tv_telefono.setText(((Pesebrera) ps.get(position)).getTelefono_pes());
    }

    public int getItemCount() {
        return ps.size();
    }
}
