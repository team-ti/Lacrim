package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearPesebrera extends AppCompatActivity {

    EditText nombre, encargado, ciudad, telefono;
    Button btn_crear;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pesebrera);

        nombre = findViewById(R.id.nombre_pes);
        encargado = findViewById(R.id.encargado_pes);
        ciudad = findViewById(R.id.ciudad_pes);
        telefono = findViewById(R.id.telefono_pes);
        btn_crear = findViewById(R.id.bt_crear_pes);

        cargarDatosToken();

        btn_crear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    public void cargarDatosToken() {
        token = getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void validarcampos() {

        if (nombre == null || nombre.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Nombre no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (encargado == null || encargado.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Encargado no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (ciudad == null || ciudad.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Ciudad no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (telefono == null || telefono.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Teléfono no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else {
            insertar_pesebrera();
        }
    }


    private void insertar_pesebrera() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre_pes", nombre.getText().toString());
        map.put("encargado_pes", encargado.getText().toString());
        map.put("ciudad_pes", ciudad.getText().toString());
        map.put("telefono_pes", telefono.getText().toString());
        map.put("id_user", token);
        registrarPesebrera(map);


    }


    private void registrarPesebrera(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url_server)+"pesebreras/pesebrera_registrar",
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
                    Toast.makeText(getApplicationContext(),"Pesebrera registrada",Toast.LENGTH_SHORT).show();
                    //FragmentPesebreras fragmentPesebreras = new FragmentPesebreras();
                   // fragmentPesebreras.ChangeAdapterPesebrera(pese);
                    //finish
                    break;



                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();


                    break;
            }
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("vista","hola");
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("vista","hola");
        startActivity(i);
        finish();
    }
}
