package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Solicitudes;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;


public class solicitud extends Fragment {

    View view;
    ArrayList<Solicitudes> Listarsolicitudes;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista_solicitud;
    String token;
    TextView txt_sin_notificacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_solicitud, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        R_lista_solicitud =  view.findViewById(R.id.rcv_solicitudes);
        R_lista_solicitud.setLayoutManager(this.mLayoutManager);
        txt_sin_notificacion = view.findViewById(R.id.txt_sin_notificacion);

        cargarDatosToken();


        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);
        consultarLista();


        R_lista_solicitud.setAdapter(new Adaptador_solicitud(Listarsolicitudes));


        return view;

    }


    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {

        Log.d("solicitud", "hola");

        Listarsolicitudes = new ArrayList<>();

        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={token};
        Solicitudes solicitud=null;

        Cursor cursor;

        try{
        cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_SOLICITUD+ " WHERE " +Constantes.CAMPO_ID_USER_RECEPTOR_SOLICITUD+"=?",parametros);


        //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

            while (cursor.moveToNext()) {
                solicitud = new Solicitudes();

                solicitud.setId_equino(cursor.getInt(1));
                solicitud.setId_user(cursor.getInt(2));
                solicitud.setId_pesebrera(cursor.getInt(3));
                solicitud.setNombre_solicitud(cursor.getString(5));


                Listarsolicitudes.add(solicitud);
            }



        cursor.close();
        }catch (Exception e){
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();

        }
    }

}
