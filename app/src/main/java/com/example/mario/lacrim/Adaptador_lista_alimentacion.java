package com.example.mario.lacrim;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mario.lacrim.Entidades.Alimentos;
import com.example.mario.lacrim.Entidades.Equinos;

import java.util.ArrayList;

public class Adaptador_lista_alimentacion extends RecyclerView.Adapter<Adaptador_lista_alimentacion.ViewHolder> {
    private ArrayList<Alimentos> al;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_lista_alimentacion(@NonNull ArrayList<Alimentos> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.al = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_nombre_list_alimentacion;
        public TextView tv_fecha_list_alimentacion;

        public ViewHolder(View v) {
            super(v);
            tv_nombre_list_alimentacion = itemView.findViewById(R.id.tv_nombre_list_alimentacion);
            tv_fecha_list_alimentacion = itemView.findViewById(R.id.tv_fecha_list_alimentacion);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }


    }


    public Adaptador_lista_alimentacion(ArrayList<Alimentos> pan) {
        al = pan;
    }

    public Adaptador_lista_alimentacion.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Adaptador_lista_alimentacion.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alimentacion, parent, false));
    }

    public void onBindViewHolder(Adaptador_lista_alimentacion.ViewHolder holder, int position) {
        holder.tv_nombre_list_alimentacion.setText(((Alimentos) al.get(position)).getNombre());
        holder.tv_fecha_list_alimentacion.setText(((Alimentos) al.get(position)).getFecha_ali());

    }

    public int getItemCount() {
        return al.size();
    }
}
