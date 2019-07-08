package com.example.mario.lacrim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Alimentos;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Premios;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Premiacion extends AppCompatActivity {

    ArrayList<Premios> ListarPremios;
    CircleImageView img_foto_perfil_equino_premiacion;
    String id_equino, nombre_equino;
    RecyclerView R_lista_premiacion;
    TextView txt_detalle_equino_premiacion;
    FloatingActionButton Fbutton_premiacion;
    private RecyclerView.LayoutManager mLayoutManager;
    String interfaz;
    ProgressDialog progressDialog;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios);


        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");
        nombre_equino = getIntent().getExtras().getString("nombre_equino");

        img_foto_perfil_equino_premiacion = findViewById(R.id.img_foto_perfil_equino_premiacion);
        R_lista_premiacion = findViewById(R.id.R_lista_premiacion);
        mLayoutManager = new LinearLayoutManager(this);
        txt_detalle_equino_premiacion = findViewById(R.id.txt_detalle_equino_premiacion);
        Fbutton_premiacion = findViewById(R.id.Fbutton_premiacion);
      //  R_lista_premiacion.setLayoutManager(this.mLayoutManager);

        txt_detalle_equino_premiacion.setText(nombre_equino);

        if (interfaz.equalsIgnoreCase("2")){

            Fbutton_premiacion.setVisibility(View.GONE);

        }



        consultar_equino();

        Fbutton_premiacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Crear_premio_equino.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                intent.putExtra("nombre_equino",nombre_equino);
                startActivity(intent);
                finish();
            }
        });

    }


    private void consultar_equino() {
        progressDialog = ProgressDialog.show(this, "Cargando premios", "Espere unos segundos");

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getResources().getString(R.string.url_server) + "premios/premios_equino/" + id_equino;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                String nombre_premiacion = "";
                                String fecha_premiacion = "";
                                String id_premiacion = "";

                                JSONArray data = new JSONArray(response);

                                ListarPremios = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    nombre_premiacion = data.getJSONObject(i).getString("nombre");
                                    fecha_premiacion = data.getJSONObject(i).getString("fecha_pre");
                                    id_premiacion = data.getJSONObject(i).getString("id_premios");

                                    Premios premios = new Premios();

                                    premios.setNombre(nombre_premiacion);
                                    premios.setFecha_ali(fecha_premiacion);
                                    premios.setId_premio(id_premiacion);

                                    ListarPremios.add(premios);
                                }
                                //procesarRespuesta(id, cod);

                                Adaptador_lista_premiacion myAdapter = new Adaptador_lista_premiacion(ListarPremios, new RecyclerViewOnItemClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent = new Intent(getApplicationContext(), Detalle_premiacion.class);
                                        intent.putExtra("id", ListarPremios.get(position).getId_premio());
                                        intent.putExtra("interfaz", interfaz);
                                        intent.putExtra("nombre_equino", nombre_equino);
                                        startActivity(intent);
                                    }
                                });

                                R_lista_premiacion.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                R_lista_premiacion.setHasFixedSize(true);
                                R_lista_premiacion.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                progressDialog.dismiss();

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
        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }


}
