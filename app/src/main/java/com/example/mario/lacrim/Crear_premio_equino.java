package com.example.mario.lacrim;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;
import com.example.mario.lacrim.Utilidades.GeneralMethodClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Crear_premio_equino extends AppCompatActivity {

    EditText ed_nombre_premio, ed_fecha_premio, ed_descripcion_premio;
    Button bt_registrar_premio;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String id_equino;
    String interfaz;
    String nombre_equino;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_premio_equino);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");

        ed_nombre_premio = findViewById(R.id.ed_nombre_premio);
        ed_descripcion_premio = findViewById(R.id.ed_descripcion_premio);
        ed_fecha_premio = findViewById(R.id.ed_fecha_premio);

        bt_registrar_premio = findViewById(R.id.bt_registrar_premio);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEdit();
            }
        };

        ed_fecha_premio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralMethodClass.hideKeyboard(Crear_premio_equino.this);
                new DatePickerDialog(view.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_registrar_premio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();


            }
        });

    }

    private void updateEdit() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_fecha_premio.setText(sdf.format(calendar.getTime()));
    }


    private void validarcampos(){

        if (ed_nombre_premio == null || ed_nombre_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Nombre no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_fecha_premio == null || ed_fecha_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Fecha no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_descripcion_premio == null || ed_descripcion_premio.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Descripcion no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else{
            insertarPremio();
        }

    }

    private void insertarPremio() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre", ed_nombre_premio.getText().toString());
        map.put("fecha_pre", ed_fecha_premio.getText().toString());
        map.put("descripcion", ed_descripcion_premio.getText().toString());
        map.put("id_equino",id_equino);


        registrarPremioEquino(map);


    }

    private void registrarPremioEquino(HashMap<String, String> map) {
        progressDialog = ProgressDialog.show(this, "Creando premio", "Espere unos segundos");

        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url_server)+"premios/premio_registrar",
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
                                progressDialog.dismiss();

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
                    Toast.makeText(getApplicationContext(),"Premio resgistrado",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, Premiacion.class);
                    i.putExtra("id",id_equino);
                    i.putExtra("interfaz",interfaz);
                    i.putExtra("nombre_equino",nombre_equino);

                    startActivity(i);
                    finish();

                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();


                    break;
            }
            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void onBackPressed() {
        Intent i = new Intent(this, Premiacion.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }

}
