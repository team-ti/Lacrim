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

public class Alimentacion extends AppCompatActivity {

    ArrayList<Alimentos> ListarAlimentos;
    ConexionSQLiteHelper conn;
    String id_equino;
    CircleImageView img_foto_perfil_equino_alimentacion;
    RecyclerView R_lista_alimentacion;
    TextView txt_detalle_equino_alimento;
    FloatingActionButton Fbutton_alimentos;
    private RecyclerView.LayoutManager mLayoutManager;
    String interfaz, nombre_equino;
    ProgressDialog progressDialog;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentacion);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");
        nombre_equino = getIntent().getExtras().getString("nombre_equino");


        img_foto_perfil_equino_alimentacion = findViewById(R.id.img_foto_perfil_equino_alimentacion);
        R_lista_alimentacion = findViewById(R.id.R_lista_alimentacion);
        mLayoutManager = new LinearLayoutManager(this);
        txt_detalle_equino_alimento = findViewById(R.id.txt_detalle_equino_alimento);
        Fbutton_alimentos = findViewById(R.id.Fbutton_alimentos);
        R_lista_alimentacion.setLayoutManager(this.mLayoutManager);
        txt_detalle_equino_alimento.setText(nombre_equino);

        if (interfaz.equalsIgnoreCase("2")){

            Fbutton_alimentos.setVisibility(View.GONE);

        }


        consultarListaAlimentacion();

        Fbutton_alimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Crear_alimento_equino.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();

            }
        });

    }



    private void consultarListaAlimentacion() {
        try {
            progressDialog = ProgressDialog.show(this, "Cargando alimentación", "Espere unos segundos");

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getResources().getString(R.string.url_server) + "alimentacion/alimentacion_equino/" + id_equino;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                String nombre_al = "";
                                String fecha_al = "";
                                String id_al = "";

                                JSONArray data = new JSONArray(response);

                                ListarAlimentos = new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    nombre_al = data.getJSONObject(i).getString("nombre");
                                    fecha_al = data.getJSONObject(i).getString("fecha_ali");
                                    id_al = data.getJSONObject(i).getString("id_alimento");

                                    Alimentos alimentos = new Alimentos();

                                    alimentos.setNombre(nombre_al);
                                    alimentos.setFecha_ali(fecha_al);
                                    alimentos.setId_alimento(id_al);

                                    ListarAlimentos.add(alimentos);
                                }
                                //procesarRespuesta(id, cod);

                                Adaptador_lista_alimentacion myAdapter = new Adaptador_lista_alimentacion(ListarAlimentos, new RecyclerViewOnItemClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent = new Intent(getApplicationContext(), Detalle_alimentacion.class);
                                        intent.putExtra("id_alimento", ListarAlimentos.get(position).getId_alimento());
                                        intent.putExtra("interfaz", interfaz);
                                        intent.putExtra("id", id_equino);
                                        intent.putExtra("nombre_equino", nombre_equino);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                R_lista_alimentacion.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                R_lista_alimentacion.setHasFixedSize(true);
                                R_lista_alimentacion.setAdapter(myAdapter);
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
                    // progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }
    }



    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }

}
