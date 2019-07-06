package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;



public class inicio extends Fragment {


    FloatingActionButton Fbutton;
    View view;
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token;

    public inicio() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_inicio, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        Fbutton =  view.findViewById(R.id.Fbutton);
        R_lista =  view.findViewById(R.id.R_lista);
        //R_lista.setLayoutManager(this.mLayoutManager);

        cargarDatosToken();

        consultarLista();


       // R_lista.setAdapter(new Adaptador_lista(ListarEquinos));

        Fbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Crear_equino.class));
                getActivity().finish();
            }
        });



        return view;
    }


    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {

        final String id_usuario = token;

        try {

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url =getResources().getString(R.string.url_server)+"equino/obtener_equinos/"+id_usuario;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                String  id_equino="";
                                String nombre = "";
                                String sexo = "";
                                String andar = "";
                                String color = "";


                                JSONArray data = new JSONArray(response);

                                ListarEquinos= new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    id_equino = data.getJSONObject(i).getString("id_equino");
                                    nombre = data.getJSONObject(i).getString("nombre_equino");
                                    sexo = data.getJSONObject(i).getString("sexo_equino");
                                    andar = data.getJSONObject(i).getString("andar_equino");
                                    color = data.getJSONObject(i).getString("color_equino");

                                    Equinos equino = new Equinos();

                                    equino.setId_equino(id_equino);
                                    equino.setNombre_equino(nombre);
                                    equino.setSexo_equino(sexo);
                                    equino.setAndar_equino(andar);
                                    equino.setColor_equino(color);

                                    ListarEquinos.add(equino);
                                }
                                //procesarRespuesta(id, cod);

                                 Adaptador_lista myAdapter = new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
                                     @Override
                                     public void onClick(View v, int position) {
                                         Intent intent = new Intent(getActivity(), Detalle_equino.class);
                                         intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                                         intent.putExtra("interfaz","1");
                                         //int id = ListarEquinos.get(position).getId_equino();
                                         //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
                                         startActivity(intent);
                                     }
                                 });

                                 R_lista.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                                 R_lista.setHasFixedSize(true);
                                 R_lista.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }


    }


}

