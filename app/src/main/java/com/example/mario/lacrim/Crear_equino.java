package com.example.mario.lacrim;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Crear_equino extends AppCompatActivity {

    EditText ed_nombre, ed_fecha_nacimiento, ed_lugar_nacimiento, ed_color, ed_microship, ed_criador, ed_propietario;
    Spinner sp_sexo, sp_tipo, sp_andar;
    Button bt_registrar;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    String token;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equino);

        ed_nombre = findViewById(R.id.ed_nombre);
        ed_fecha_nacimiento = findViewById(R.id.ed_fecha_nacimiento);
        ed_lugar_nacimiento = findViewById(R.id.ed_lugar_nacimiento);
        ed_color = findViewById(R.id.ed_color);
        ed_microship = findViewById(R.id.ed_microship);
        ed_criador = findViewById(R.id.ed_criador);
        ed_propietario = findViewById(R.id.ed_propietario);
        sp_sexo = findViewById(R.id.sp_sexo);
        sp_tipo = findViewById(R.id.sp_tipo);
        sp_andar = findViewById(R.id.sp_andar);
        bt_registrar = findViewById(R.id.bt_registrar);

        cargarDatosToken();

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


        ed_fecha_nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralMethodClass.hideKeyboard(Crear_equino.this);
                new DatePickerDialog(view.getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        bt_registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    public void cargarDatosToken() {
        token = getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void updateEdit() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed_fecha_nacimiento.setText(sdf.format(calendar.getTime()));
    }

    private void validarcampos(){

        if (ed_nombre == null || ed_nombre.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Nombre no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_fecha_nacimiento == null || ed_fecha_nacimiento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Fecha de nacimiento no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_lugar_nacimiento == null || ed_lugar_nacimiento.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Lugar de nacimiento no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_color == null || ed_color.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Color no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_microship == null || ed_microship.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Microship no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(ed_criador == null || ed_criador.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Criador no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if (ed_propietario == null || ed_propietario.getText().toString().equals("")){

            Toast.makeText(getApplicationContext(),"Propietario no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(sp_sexo == null || sp_sexo.getSelectedItem().toString().equals("--Seleccionar sexo--")){

            Toast.makeText(getApplicationContext(),"Sexo no puede estar vacio",Toast.LENGTH_SHORT).show();


        }else if (sp_tipo == null || sp_tipo.getSelectedItem().toString().equals("--Seleccionar tipo--")){

            Toast.makeText(getApplicationContext(),"Tipo no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else if(sp_andar == null || sp_andar.getSelectedItem().toString().equals("--Seleccionar andar--")){

            Toast.makeText(getApplicationContext(),"Andar no puede estar vacio",Toast.LENGTH_SHORT).show();

        }else{
            insertar_equino();
        }

    }

    private void insertar_equino() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre_equino", ed_nombre.getText().toString());
        map.put("fecha_equino", ed_fecha_nacimiento.getText().toString());
        map.put("color_equino", ed_color.getText().toString());
        map.put("microship_equino", ed_microship.getText().toString());
        map.put("criador_equino", ed_criador.getText().toString());
        map.put("propietario_equino", ed_propietario.getText().toString());
        map.put("sexo_equino", sp_sexo.getSelectedItem().toString());
        map.put("tipo_equino", sp_tipo.getSelectedItem().toString());
        map.put("andar_equino", sp_andar.getSelectedItem().toString());
        map.put("lugar_equino", ed_lugar_nacimiento.getText().toString());
        map.put("avatar_equino", "");
        map.put("id_usuario",token);


        registrarEquino(map);


    }


    private void registrarEquino(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);
        progressDialog = ProgressDialog.show(this, "Creando equino", "Espere unos segundos");


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url_server)+"equino/equino_registrar",
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
                    Toast.makeText(getApplicationContext(),"Equino registrado",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Crear_equino.this, MainActivity.class));
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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
