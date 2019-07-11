package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Solicitudes;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class solicitud extends Fragment {

    View view;
    ArrayList<Solicitudes> Listarsolicitudes;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista_solicitud;
    String token;
    Context mContext;
    TextView txt_sin_notificacion;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_solicitud, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        R_lista_solicitud =  view.findViewById(R.id.rcv_solicitudes);
        R_lista_solicitud.setLayoutManager(this.mLayoutManager);
        txt_sin_notificacion = view.findViewById(R.id.txt_sin_notificacion);

        mContext = this.getContext();

        progressDialog = ProgressDialog.show(getActivity(), "Cargando solicitudes", "Espere unos segundos");


        cargarDatosToken();


        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);
        consultarLista();


        //R_lista_solicitud.setAdapter(new Adaptador_solicitud(Listarsolicitudes));


        return view;

    }


    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {
        final String id_usuario = token;
        Listarsolicitudes = new ArrayList<>();

        try {

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url =getResources().getString(R.string.url_server)+"solicitud/obtener_solicitudes/"+token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Integer id_equino=0;
                            Integer id_user = 0;
                            Integer id_pesebrera = 0;
                            String nombre_solicitud = "";
                            Integer id_solicitud = 0;
                            String avatar="";

                            JSONArray data = new JSONArray(response);

                            if (data.length()>0){

                                R_lista_solicitud.setVisibility(View.VISIBLE);
                                txt_sin_notificacion.setVisibility(View.GONE);

                            }else{

                                R_lista_solicitud.setVisibility(View.GONE);
                                txt_sin_notificacion.setVisibility(View.VISIBLE);
                            }



                            Listarsolicitudes= new ArrayList<>();


                            for (int i = 0; i < data.length(); i++) {
                                id_equino = data.getJSONObject(i).getInt("id_equino");
                                id_user = data.getJSONObject(i).getInt("id_usuario");
                                id_pesebrera = data.getJSONObject(i).getInt("id_pesebrera");
                                nombre_solicitud = data.getJSONObject(i).getString("nombre");
                                id_solicitud = data.getJSONObject(i).getInt("id_solicitud");
                                avatar = data.getJSONObject(i).getString("avatar");
                                Solicitudes solicitud = new Solicitudes();

                                solicitud.setId_equino(id_equino);
                                solicitud.setId_user(id_user);
                                solicitud.setId_pesebrera(id_pesebrera);
                                solicitud.setNombre_solicitud(nombre_solicitud);
                                solicitud.setId_solicitud(id_solicitud);
                                solicitud.setAvatar(avatar);



                                Listarsolicitudes.add(solicitud);
                            }

                            Adaptador_solicitud myAdapter = new Adaptador_solicitud(mContext, Listarsolicitudes);
                            R_lista_solicitud.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            R_lista_solicitud.setHasFixedSize(true);
                            R_lista_solicitud.setAdapter(myAdapter);
                            //procesarRespuesta(id, cod);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            R_lista_solicitud.setVisibility(View.GONE);
                            txt_sin_notificacion.setVisibility(View.VISIBLE);
                        }
                         progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();
                R_lista_solicitud.setVisibility(View.GONE);
                txt_sin_notificacion.setVisibility(View.VISIBLE);
            }
        });

            queue.add(stringRequest);
        } catch (Exception e) {

        }


    }

}
