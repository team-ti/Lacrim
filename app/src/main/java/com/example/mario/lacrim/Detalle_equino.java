package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalle_equino extends AppCompatActivity {

    TextView txt_detalle_equino;
    String id_equino, nombre_equino;
    LinearLayout ln_datos_generales, ln_premios, ln_alimentos,ln_videos;
    String interfaz;
    CircleImageView img_foto_perfil_equino_detalle;
    ProgressDialog progressDialog;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_equino);

        img_foto_perfil_equino_detalle = findViewById(R.id.img_foto_perfil_equino_detalle);
        txt_detalle_equino = findViewById(R.id.txt_detalle_equino);
        ln_datos_generales = findViewById(R.id.ln_datos_generales);
        ln_premios = findViewById(R.id.ln_premios);
        ln_alimentos = findViewById(R.id.ln_alimentos);
        ln_videos = findViewById(R.id.ln_videos);

        progressDialog = ProgressDialog.show(this, "Cargando equino", "Espere unos segundos");


        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");



        ln_datos_generales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Datos_generales.class);

                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);

                startActivity(intent);
                finish();

            }
        });

        ln_premios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Premiacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                intent.putExtra("nombre_equino",nombre_equino);
                startActivity(intent);
                finish();

            }
        });

        ln_alimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Alimentacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                intent.putExtra("nombre_equino",nombre_equino);
                startActivity(intent);
                finish();

            }
        });


        ln_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Presentacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();

            }
        });

        consultar_equino();


    }

    private void consultar_equino() {

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"equino/obtener_equino_info/"+id_equino;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                String avatar_equino = "";



                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    nombre_equino = data.getJSONObject(i).getString("nombre_equino");
                                    avatar_equino = data.getJSONObject(i).getString("avatar_equino");

                                }

                                txt_detalle_equino.setText(nombre_equino);


                                if (!avatar_equino.equalsIgnoreCase("")){

                                    String codbase64 = avatar_equino;

                                    byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                                    Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    img_foto_perfil_equino_detalle.setImageBitmap(img);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                             progressDialog.dismiss();
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


  /*  public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }*/


}