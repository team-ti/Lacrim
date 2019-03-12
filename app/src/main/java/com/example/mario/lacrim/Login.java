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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

public class Login extends AppCompatActivity {
    Button btnEntrar;
    EditText lo_usuario, lo_contrasena;
    TextView btnRegistrar_lo;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cargarDatos();

        lo_usuario = findViewById(R.id.lo_usuario);
        lo_contrasena = findViewById(R.id.lo_contrasena);
        btnEntrar = findViewById(R.id.btn_entrar);
        btnRegistrar_lo = findViewById(R.id.btnRegistrar_lo);
        sharedpreferences = getSharedPreferences(dataUserCache, modo_private);
        editor = sharedpreferences.edit();

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
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor cursor;
        String usuario = lo_usuario.getText().toString().trim();
        String contrasena = lo_contrasena.getText().toString().trim();
        String[] columns = {Constantes.CAMPO_ID, Constantes.CAMPO_USER, Constantes.CAMPO_CONTRASENA};
        String selection = Constantes.CAMPO_USER+" = ? AND " + Constantes.CAMPO_CONTRASENA +" = ?";
        String[] selectionArgs = {usuario, contrasena};
        String tableName = Constantes.TABLA_USUARIO;
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()==true) {
            String id = cursor.getString(0);
            String usua=cursor.getString(1);
            String contra=cursor.getString(2);
            Log.d("id:", id);
            if (usua.equals(lo_usuario.getText().toString().trim()) && contra.equals(lo_contrasena.getText().toString().trim())){
                editor.putString("access_token", id);
                editor.commit();
                Intent principal=new Intent(this,MainActivity.class);
                startActivity(principal);
                finish();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}
