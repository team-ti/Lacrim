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
import android.widget.EditText;
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

public class  Detalle_premiacion extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText ed_nombre_detalle_premio, ed_fecha_detalle_premio, ed_descripcion_detalle_premio;
    String id_equino;
    String interfaz;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_premiacion);

        ed_nombre_detalle_premio = findViewById(R.id.ed_nombre_detalle_premio);
        ed_fecha_detalle_premio = findViewById(R.id.ed_fecha_detalle_premio);
        ed_descripcion_detalle_premio = findViewById(R.id.ed_descripcion_detalle_premio);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");

        consultar_premio();

    }

    private void consultar_premio() {
        progressDialog = ProgressDialog.show(this, "Cargando datos premio", "Espere unos segundos");

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String id_premio = getIntent().getExtras().getString("id");
            String url =getResources().getString(R.string.url_server)+"premios/premio_info/"+id_premio;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONArray data = new JSONArray(response);
                                String nombre="";
                                String fecha_pre="";
                                String descripcion = "";

                                for (int i = 0; i < data.length(); i++) {
                                    nombre = data.getJSONObject(i).getString("nombre");
                                    fecha_pre = data.getJSONObject(i).getString("fecha_pre");
                                    descripcion = data.getJSONObject(i).getString("descripcion");

                                }

                                ed_descripcion_detalle_premio.setText(descripcion);
                                ed_nombre_detalle_premio.setText(nombre);
                                ed_fecha_detalle_premio.setText(fecha_pre);
                                progressDialog.dismiss();

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

    public void onBackPressed() {
        Intent i = new Intent(this, Premiacion.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        i.putExtra("nombre_equino",getIntent().getExtras().getString("nombre_equino"));
        startActivity(i);
        finish();
    }

}
