package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatosPesebrera extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText txt_nombre, txt_encargado, txt_ciudad, txt_telefono;
    Button btn_actualizar;
    String id_pes;
    String interfaz;
    String nombre_pes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_pesebrera);

        txt_nombre = findViewById(R.id.ed_nombre_pes);
        txt_encargado = findViewById(R.id.ed_encargado_pes);
        txt_ciudad = findViewById(R.id.ed_ciudad_pes);
        txt_telefono = findViewById(R.id.ed_telefono_pes);
        btn_actualizar = findViewById(R.id.bt_actualizar_pes);

        id_pes = getIntent().getExtras().getString("id");

        interfaz = getIntent().getExtras().getString("interfaz");

        consultar();

        if (interfaz.equalsIgnoreCase("2")) {

            btn_actualizar.setVisibility(View.GONE);
            txt_nombre.setEnabled(false);
            txt_encargado.setEnabled(false);
            txt_ciudad.setEnabled(false);
            txt_telefono.setEnabled(false);

        }
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    private void consultar() {
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"pesebreras/obtener_pesebrera_info/"+id_pes;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                String nombre = "";
                                String encargado = "";
                                String ciudad = "";
                                String telefono= "";




                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    nombre = data.getJSONObject(i).getString("nombre_pes");
                                    encargado = data.getJSONObject(i).getString("encargado_pes");
                                    ciudad = data.getJSONObject(i).getString("ciudad_pes");
                                    telefono = data.getJSONObject(i).getString("telefono_pes");

                                }

                                txt_nombre.setText(nombre);
                                txt_encargado.setText(encargado);
                                txt_ciudad.setText(ciudad);
                                txt_telefono.setText(telefono);


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

    private void validarcampos() {

        if (txt_nombre == null || txt_nombre.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Nombre no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (txt_encargado == null || txt_encargado.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Encargado no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (txt_ciudad == null || txt_ciudad.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Ciudad no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (txt_telefono == null || txt_telefono.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Teléfono no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else {
            insertar_pesebrera();
        }
    }

    private void insertar_pesebrera() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre_pes", txt_nombre.getText().toString());
        map.put("encargado_pes", txt_encargado.getText().toString());
        map.put("ciudad_pes", txt_ciudad.getText().toString());
        map.put("telefono_pes", txt_telefono.getText().toString());
        map.put("id_pes", id_pes);
        actualizarPesebrera(map);


    }


    private void actualizarPesebrera(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.PUT,
                        getResources().getString(R.string.url_server)+"pesebreras/pesebrera_actualizar",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_insert(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "Error Volley: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void procesarRespuesta_insert(JSONObject response) {

        try {
            String cod = response.getString("cod");

            switch (cod) {
                case "1":
                    Toast.makeText(getApplicationContext(),"Pesebrera actualizada",Toast.LENGTH_SHORT).show();
                    FragmentPesebreras fragmentPesebreras = new FragmentPesebreras();

                     //fragmentPesebreras.ChangeAdapterPesebrera(pese);
                    //finish
                    break;



                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void onBackPressed() {
        Intent i = new Intent(this, DetallePesebrera.class);
        i.putExtra("id",id_pes);
        i.putExtra("interfaz",interfaz);
        i.putExtra("nombre_pes", getIntent().getExtras().getString("nombre_pes"));
        startActivity(i);
        finish();
    }
}
