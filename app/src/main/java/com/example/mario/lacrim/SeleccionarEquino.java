package com.example.mario.lacrim;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeleccionarEquino extends AppCompatActivity {
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView txt_sin_equinos;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token, equino_id, id_pes, cod, nombre_usuario;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_equino);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        txt_sin_equinos = findViewById(R.id.txt_sin_equinos);
        R_lista =  findViewById(R.id.R_lista_selecciona_equino);
       // R_lista.setLayoutManager(this.mLayoutManager);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cargarDatosToken();

        id_pes = getIntent().getExtras().getString("id");
        consultarPesebreraUser();
        consultarLista();
        consultar_nombre();



        //R_lista.setAdapter(new Adaptador_seleccionar_equino(ListarEquinos));
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    public void consultarPesebreraUser() {
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getResources().getString(R.string.url_server) + "pesebreras/validar_pesebrera/" + token + "/" + id_pes;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject data = new JSONObject(response);
                                cod = data.getString("cod");


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

    private void consultar_nombre() {
        final String id_usuario = token;

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"generic/usuario_info/"+id_usuario;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                String nom= "";
                                String ape= "";

                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    nom = data.getJSONObject(i).getString("nombre");
                                    ape = data.getJSONObject(i).getString("apellido");
                                }
                                nombre_usuario = nom + " " + ape;

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


    private void consultarLista() {
        ListarEquinos = new ArrayList<>();

        final String id_usuario = token;
        progressDialog = ProgressDialog.show(this, "Cargando equinos", "Espere unos segundos");

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"equino/obtener_equinos/"+id_usuario;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                String  id_equino="";
                                String nombre = "";
                                String sexo = "";
                                String andar = "";
                                String color = "";


                                JSONArray data = new JSONArray(response);


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

                                Adaptador_seleccionar_equino myAdapter = new Adaptador_seleccionar_equino(ListarEquinos, new RecyclerViewOnItemClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        equino_id =ListarEquinos.get(position).getId_equino();
                                        Log.d("cod", cod);
                                        if (cod.equalsIgnoreCase("2")) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SeleccionarEquino.this);
                                            builder1.setMessage("¿Quieres enviar solicitud con este equino? ");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Si",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            insertar_equino_solicitud();

                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    "No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }else{
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SeleccionarEquino.this);
                                            builder1.setMessage("¿Quieres agregar este equino a su pesebrera? ");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Si",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            insertar_equino();

                                                        }
                                                    });

                                            builder1.setNegativeButton(
                                                    "No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }
                                    }
                                });

                                R_lista.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                R_lista.setHasFixedSize(true);
                                R_lista.setAdapter(myAdapter);

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

    private void insertar_equino() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_pesebrera", id_pes);
        map.put("id_equino", equino_id);


        agregarEquino(map);


    }

    private void agregarEquino(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.PUT,
                        getResources().getString(R.string.url_server)+"solicitud/solicitud_directa",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_actualizar(response);
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
    private void procesarRespuesta_actualizar(JSONObject response) {

        try {
            String cod = response.getString("cod");


            switch (cod) {
                case "1":
                    Toast.makeText(getApplicationContext(), "Acción realizada", Toast.LENGTH_SHORT).show();

                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertar_equino_solicitud() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_equino",equino_id);
        map.put("id_usuario",token);
        map.put("id_pesebrera", id_pes);
        map.put("nombre", nombre_usuario);
        map.put("id_usuario_receptor", getIntent().getExtras().getString("id_user"));

        enviarSolicitud(map);


    }




    private void enviarSolicitud(HashMap<String, String> map){
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url_server)+"solicitud/insertar_solicitud",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_insert_solicitud(response);
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
    private void procesarRespuesta_insert_solicitud(JSONObject response) {

        try {
            String cod = response.getString("cod");

            switch (cod) {
                case "1":
                    Toast.makeText(getApplicationContext(),"Solicitud enviada",Toast.LENGTH_SHORT).show();

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


}
