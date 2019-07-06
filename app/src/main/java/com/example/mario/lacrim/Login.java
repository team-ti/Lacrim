package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Usuario;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    Button btnEntrar;
    EditText lo_usuario, lo_contrasena;
    TextView btnRegistrar_lo;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    RadioButton rb_sesion;

    private boolean isActivateRadioButton;
    private static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado";

    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //cargarDatos();

        lo_usuario = findViewById(R.id.lo_usuario);
        lo_contrasena = findViewById(R.id.lo_contrasena);
        rb_sesion = findViewById(R.id.rb_sesion);
        btnEntrar = findViewById(R.id.btn_entrar);
        btnRegistrar_lo = findViewById(R.id.btnRegistrar_lo);
        sharedpreferences = getSharedPreferences(dataUserCache, modo_private);
        editor = sharedpreferences.edit();

        if (obtenerEstadoButton()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnRegistrar_lo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Login.this, Crear_usuario.class));
                finish();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    public void cargarDatos() {

        token = this.getSharedPreferences(dataUserCache,modo_private).getString("access_token", "0");
        Log.d("token", token);
        if (!token.equalsIgnoreCase("0")){

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public static void changeEstadoButton(Context c, boolean b) {
        c.getSharedPreferences(dataUserCache, 0).edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION, b).apply();
    }

    public void guardarEstadoButton() {
        getSharedPreferences(dataUserCache, 0).edit().putBoolean(PREFERENCE_ESTADO_BUTTON_SESION, rb_sesion.isChecked()).apply();
    }

    public boolean obtenerEstadoButton() {
        return getSharedPreferences(dataUserCache, 0).getBoolean(PREFERENCE_ESTADO_BUTTON_SESION, false);
    }


    private void validarCampos(){
        if (lo_usuario == null || lo_usuario.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Usuario no puede estar vacio", Toast.LENGTH_SHORT).show();
        } else if (lo_contrasena == null || lo_contrasena.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Contraseña no puede estar vacio", Toast.LENGTH_SHORT).show();
        } else {
            inicarSesion();
        }
    }

    private void inicarSesion(){
        String usuario = lo_usuario.getText().toString().replace(" ","");
        String contra = lo_contrasena.getText().toString().replace(" ","");

        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url =getResources().getString(R.string.url_server)+"generic/usuario_login/"+usuario+"/"+contra;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                int id = 0;
                                String cod = "";
                                //arraylogin = new ArrayList<>();
                                JSONArray data = new JSONArray(response);
                                for (int i = 0; i < data.length(); i++) {
                                    id = data.getJSONObject(i).getInt("id_usuario");
                                    cod = data.getJSONObject(i).getString("cod");

                                }
                                    validarIngreso(id, cod);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           // progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();

                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void validarIngreso(int id, String cod){
        Log.d("response", "onResponse: "+cod);
        if(cod.equalsIgnoreCase("1")) {
            editor.putString("access_token", Integer.toString(id));
            guardarEstadoButton();
            editor.commit();
            Intent principal=new Intent(this,MainActivity.class);
            startActivity(principal);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
}
