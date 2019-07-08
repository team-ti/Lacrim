package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

public class Detalle_alimentacion extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText ed_nombre_detalle_alimentacion, ed_fecha_detalle_alimentacion, ed_descripcion_detalle_alimentacion;
    String id_equino, id_alimento;
    String interfaz;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alimentacion);

        ed_nombre_detalle_alimentacion = findViewById(R.id.ed_nombre_detalle_alimentacion);
        ed_fecha_detalle_alimentacion = findViewById(R.id.ed_fecha_detalle_alimentacion);
        ed_descripcion_detalle_alimentacion = findViewById(R.id.ed_descripcion_detalle_alimentacion);

        id_equino = getIntent().getExtras().getString("id");
        id_alimento = getIntent().getExtras().getString("id_alimento");
        interfaz = getIntent().getExtras().getString("interfaz");


        consultar_alimentacion();

    }

    private void consultar_alimentacion() {
        try {
            progressDialog = ProgressDialog.show(this, "Cargando datos alimentación", "Espere unos segundos");


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            //String id_alimentacion = getIntent().getExtras().getString("id");
            String url =getResources().getString(R.string.url_server)+"alimentacion/alimentacion_info/"+id_alimento;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray data = new JSONArray(response);
                                String nombre="";
                                String fecha_ali="";
                                String descripcion = "";

                                for (int i = 0; i < data.length(); i++) {
                                    nombre = data.getJSONObject(i).getString("nombre");
                                    fecha_ali = data.getJSONObject(i).getString("fecha_ali");
                                    descripcion = data.getJSONObject(i).getString("descripcion");

                                }

                                ed_descripcion_detalle_alimentacion.setText(descripcion);
                                ed_nombre_detalle_alimentacion.setText(nombre);
                                ed_fecha_detalle_alimentacion.setText(fecha_ali);

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
                    // progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }

    }

    public void onBackPressed() {
        Intent i = new Intent(this, Alimentacion.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        i.putExtra("nombre_equino",getIntent().getExtras().getString("nombre_equino"));

        startActivity(i);
        finish();
    }

}
