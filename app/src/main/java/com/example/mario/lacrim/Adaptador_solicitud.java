package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;
import com.example.mario.lacrim.Entidades.Solicitudes;

import java.util.ArrayList;


/////////

public class Adaptador_solicitud extends RecyclerView.Adapter<Adaptador_solicitud.MyViewHolder> implements View.OnClickListener{

    private ArrayList<Solicitudes> sl;
    ConexionSQLiteHelper conn;
    int id_equino;
    int id_pesebrera;
    private Context mContext;
    private View.OnClickListener listener;




    public Adaptador_solicitud(Context mContext, ArrayList<Solicitudes> sl) {
        this.mContext = mContext;
        this.sl = sl;
    }

    @NonNull
    @Override
    public Adaptador_solicitud.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_solicitud, viewGroup, false);
        view.setOnClickListener(this);
        return new Adaptador_solicitud.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adaptador_solicitud.MyViewHolder myViewHolder, final int i) {

        String nombre = sl.get(i).getNombre_solicitud() + " te ha enviado una solicitud";

        myViewHolder.tv_nombre_notificacion.setText(nombre);



        id_equino = sl.get(i).getId_equino();
        id_pesebrera = sl.get(i).getId_pesebrera();

        String avatar = cargaravatar();

        if (!avatar.equalsIgnoreCase("")){

            String codbase64 = avatar;

            byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
            Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            myViewHolder.img_perfil_notificacion.setImageBitmap(img);

        }


        myViewHolder.img_perfil_notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Detalle_equino.class);
                String idString;
                idString = String.valueOf(sl.get(i).getId_equino());
                intent.putExtra("id", idString);
                intent.putExtra("interfaz","2");
                mContext.startActivity(intent);
                //    getActivity().finish();

            }
        });



        myViewHolder.bt_aceptar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewHolder.bt_aceptar_solicitud.setVisibility(View.GONE);
                myViewHolder.bt_rechazar_solicitud.setVisibility(View.GONE);
                myViewHolder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                myViewHolder.texto_aceptar_rechazar.setText("Solicitud aceptada");

                AceptarSolicitud();

            }
        });


        myViewHolder.bt_rechazar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewHolder.bt_aceptar_solicitud.setVisibility(View.GONE);
                myViewHolder.bt_rechazar_solicitud.setVisibility(View.GONE);
                myViewHolder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                myViewHolder.texto_aceptar_rechazar.setText("Solicitud rechazada");

                RechazarSolicitud();

            }
        });


    }
    public void AceptarSolicitud(){

        conn=new ConexionSQLiteHelper(mContext,"bd_equinos",null,1);

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

        conn=new ConexionSQLiteHelper(mContext,"bd_equinos",null,1);


        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={String.valueOf(id_equino)};

        db.delete(Constantes.TABLA_SOLICITUD,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
        db.close();


    }

    private String cargaravatar() {

        conn=new ConexionSQLiteHelper(mContext,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={String.valueOf(id_equino)};
        String[] campos={Constantes.CAMPO_AVATAR_EQUINO};
        //Cursor cursor;

        String ava = "";

        try {

            Cursor cursor =db.query(Constantes.TABLA_EQUINO,campos,Constantes.CAMPO_ID_EQUINO+"=?",parametros,null,null,null);
            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO+" WHERE "+Constantes.CAMPO_ID_EQUINO,parametros);

            cursor.moveToFirst();

            if (!cursor.isNull(0)){

                 ava= cursor.getString(0);
                return ava;

            }

            cursor.close();

        }catch (Exception e){
            //Toast.makeText(mContext,"El equino no existe",Toast.LENGTH_LONG).show();

        }


        return ava;
    }


    @Override
    public int getItemCount() {
        return sl.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {

        if (listener!=null){
            listener.onClick(view);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_nombre_notificacion;
        public TextView texto_aceptar_rechazar;
        public Button bt_aceptar_solicitud;
        public Button bt_rechazar_solicitud;
        public ImageView img_perfil_notificacion;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_nombre_notificacion = itemView.findViewById(R.id.txt_nombre_notificacion);
            bt_aceptar_solicitud = itemView.findViewById(R.id.bt_aceptar_solicitud);
            bt_rechazar_solicitud = itemView.findViewById(R.id.bt_rechazar_solicitud);
            texto_aceptar_rechazar = itemView.findViewById(R.id.texto_aceptar_rechazar);
            img_perfil_notificacion = itemView.findViewById(R.id.img_perfil_notificacion);
        }
    }
}




