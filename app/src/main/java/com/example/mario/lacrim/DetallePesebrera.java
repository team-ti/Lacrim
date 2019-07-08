package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import org.json.JSONObject;

public class DetallePesebrera extends AppCompatActivity {

    TextView txt_detalle_pes, text_solicitud;
    String id_pesebrera;
    LinearLayout ln_datos_generales_pes, ln_lista_equinos, ln_solicitud;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    String interfaz_pes;
    String nombre_pes, id_user;
    String token, cod;
    ProgressDialog progressDialog;


    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pesebrera);

        txt_detalle_pes = findViewById(R.id.txt_detalle_pes);
        ln_datos_generales_pes = findViewById(R.id.ln_datos_generales_pes);
        ln_lista_equinos = findViewById(R.id.ln_lista_equinos);
        ln_solicitud = findViewById(R.id.ln_solicitud);
        text_solicitud = findViewById(R.id.text_solicitud);

        progressDialog = ProgressDialog.show(this, "Cargando pesebrera", "Espere unos segundos");


        cargarDatosToken();


        id_pesebrera = getIntent().getExtras().getString("id");
        interfaz_pes = getIntent().getExtras().getString("interfaz");
        nombre_pes = getIntent().getExtras().getString("nombre_pes");
        id_user = getIntent().getExtras().getString("id_user");
        txt_detalle_pes.setText(nombre_pes);
        consultarPesebreraUser();


        ln_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SeleccionarEquino.class);

                intent.putExtra("id",id_pesebrera);
                Log.d("id_pes", ""+id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);
                intent.putExtra("nombre_pes", nombre_pes);
                intent.putExtra("id_user", id_user);

                startActivity(intent);

            }
        });

        ln_datos_generales_pes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DatosPesebrera.class);

                intent.putExtra("id",id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);
                intent.putExtra("nombre_pes", nombre_pes);

                startActivity(intent);
                finish();

            }
        });


        ln_lista_equinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ListarEquinosPesebrera.class);
                intent.putExtra("id",id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);
                intent.putExtra("nombre_pes", nombre_pes);

                startActivity(intent);
                finish();

            }
        });


    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    public void consultarPesebreraUser() {
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getResources().getString(R.string.url_server) + "pesebreras/validar_pesebrera/" + token + "/" + id_pesebrera;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject data = new JSONObject(response);
                                cod = data.getString("cod");
                                if (cod.equalsIgnoreCase("1") ){
                                    text_solicitud.setText("Agregar Equino");
                                }else if (cod.equalsIgnoreCase("2")){
                                    text_solicitud.setText("Enviar solicitud");
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

}
