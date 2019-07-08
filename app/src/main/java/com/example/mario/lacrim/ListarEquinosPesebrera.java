package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListarEquinosPesebrera extends AppCompatActivity {
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token, id_pes, interfaz;
    TextView txt_sin_equino;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_equinos_pesebrera);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        R_lista =  findViewById(R.id.R_lista);
        R_lista.setLayoutManager(this.mLayoutManager);
        txt_sin_equino = findViewById(R.id.txt_sin_equino);

        cargarDatosToken();
        id_pes = getIntent().getExtras().getString("id");

        interfaz = getIntent().getExtras().getString("interfaz");

        consultarLista();


    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {
        ListarEquinos = new ArrayList<>();
        progressDialog = ProgressDialog.show(this, "Cargando equinos", "Espere unos segundos");

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"equino/equinos_pesebrera/"+id_pes;

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

                                if (data.length()>0){

                                    R_lista.setVisibility(View.VISIBLE);
                                    txt_sin_equino.setVisibility(View.GONE);

                                }else{

                                    R_lista.setVisibility(View.GONE);
                                    txt_sin_equino.setVisibility(View.VISIBLE);
                                }

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
                                        Intent intent = new Intent(getApplicationContext(), Detalle_equino.class);
                                        intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                                        intent.putExtra("interfaz","1");
                                        //int id = ListarEquinos.get(position).getId_equino();
                                        //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                });

                                R_lista.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                R_lista.setHasFixedSize(true);
                                R_lista.setAdapter(myAdapter);

                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }


    }

    public void onBackPressed() {
        Intent i = new Intent(this, DetallePesebrera.class);
        i.putExtra("id",id_pes);
        i.putExtra("interfaz",interfaz);
        i.putExtra("nombre_pes",getIntent().getExtras().getString("nombre_pes"));
        startActivity(i);
        finish();
    }


}
