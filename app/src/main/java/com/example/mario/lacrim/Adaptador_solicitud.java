package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Solicitudes;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

public class Adaptador_solicitud extends RecyclerView.Adapter<Adaptador_solicitud.ViewHolder> {
    private ArrayList<Solicitudes> sl;
    ConexionSQLiteHelper conn;
    int id_equino;
    int id_pesebrera;
    Context mContext;
    private static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener1;

    public Adaptador_solicitud(@NonNull ArrayList<Solicitudes> v, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.sl = v;
        recyclerViewOnItemClickListener1 = recyclerViewOnItemClickListener;
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_nombre_notificacion;
        public TextView texto_aceptar_rechazar;
        public Button bt_aceptar_solicitud;
        public Button bt_rechazar_solicitud;

        public ViewHolder(View v) {
            super(v);
            tv_nombre_notificacion = itemView.findViewById(R.id.txt_nombre_notificacion);
            bt_aceptar_solicitud = itemView.findViewById(R.id.bt_aceptar_solicitud);
            bt_rechazar_solicitud = itemView.findViewById(R.id.bt_rechazar_solicitud);
            texto_aceptar_rechazar = itemView.findViewById(R.id.texto_aceptar_rechazar);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener1.onClick(v, getAdapterPosition());
        }


    }


    public Adaptador_solicitud(ArrayList<Solicitudes> pan) {
        sl = pan;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_solicitud, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        String nombre = sl.get(position).getNombre_solicitud() + " te ha enviado una solicitud";

        holder.tv_nombre_notificacion.setText(nombre);

        id_equino = sl.get(position).getId_equino();
        id_pesebrera = sl.get(position).getId_pesebrera();


        holder.bt_aceptar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.bt_aceptar_solicitud.setVisibility(View.GONE);
                holder.bt_rechazar_solicitud.setVisibility(View.GONE);
                holder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                holder.texto_aceptar_rechazar.setText("Solicitud aceptada");

                AceptarSolicitud();

            }
        });


        holder.bt_rechazar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.bt_aceptar_solicitud.setVisibility(View.GONE);
                holder.bt_rechazar_solicitud.setVisibility(View.GONE);
                holder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                holder.texto_aceptar_rechazar.setText("Solicitud rechazada");

                RechazarSolicitud();

            }
        });

    }

    public int getItemCount() {
        return sl.size();
    }


    public void AceptarSolicitud(){


        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={String.valueOf(id_equino)};

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_ID_PESEBRERA_EQUINO,id_pesebrera);


        db.update(Constantes.TABLA_EQUINO,values,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
        db.close();


        EliminarSolicitud();


    }


    public void RechazarSolicitud(){

        EliminarSolicitud();

    }



    public void EliminarSolicitud(){

        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={String.valueOf(id_equino)};

        db.delete(Constantes.TABLA_EQUINO,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
        db.close();


    }


}

