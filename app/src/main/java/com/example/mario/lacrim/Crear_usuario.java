package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.regex.Pattern;

public class Crear_usuario extends AppCompatActivity {

    EditText us_nombres, us_apellidos, us_correo, us_ciudad, us_usuario, us_contrasena;
    Button btn_registrarus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        us_nombres = findViewById(R.id.us_nombres);
        us_apellidos = findViewById(R.id.us_apellidos);
        us_correo = findViewById(R.id.us_correo);
        us_ciudad = findViewById(R.id.us_ciudad);
        us_usuario = findViewById(R.id.us_usuario);
        us_contrasena = findViewById(R.id.us_contrasena);
        btn_registrarus = findViewById(R.id.btn_registrarus);

        btn_registrarus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    boolean consultarUsuario(String user) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor;
        String usuario = us_usuario.getText().toString().trim();
        String[] columns = {Constantes.CAMPO_USER};
        String selection = Constantes.CAMPO_USER + " = ?";
        String[] selectionArgs = {usuario};
        String tableName = Constantes.TABLA_USUARIO;
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst() == true) {
            return true;
        }else{
            return false;
        }
    }

    private void validarcampos() {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean match = pattern.matcher(us_correo.getText().toString()).matches();
        if (us_nombres == null || us_nombres.getText().toString().equals("")) {
            us_nombres.requestFocus();
            Toast.makeText(getApplicationContext(), "Nombres no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (us_apellidos == null || us_apellidos.getText().toString().equals("")) {
            us_apellidos.requestFocus();
            Toast.makeText(getApplicationContext(), "Apellidos no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (us_correo == null || us_correo.getText().toString().equals("")) {
            us_correo.requestFocus();
            Toast.makeText(getApplicationContext(), "Correo no puede estar vacio", Toast.LENGTH_SHORT).show();
        } else  if (!match){
            us_correo.requestFocus();
            Toast.makeText(getApplicationContext(), "Formato de correo incorreto", Toast.LENGTH_SHORT).show();

        } else if (us_ciudad == null || us_ciudad.getText().toString().equals("")) {
            us_ciudad.requestFocus();
            Toast.makeText(getApplicationContext(), "Ciudad no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (us_usuario == null || us_usuario.getText().toString().equals("")) {
            us_usuario.requestFocus();
            Toast.makeText(getApplicationContext(), "Usuario no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (consultarUsuario(us_usuario.getText().toString())){
            us_usuario.requestFocus();
            Toast.makeText(getApplicationContext(), "Usuario ya existe", Toast.LENGTH_SHORT).show();

        } else if (us_contrasena == null || us_contrasena.getText().toString().equals("")) {
            us_contrasena.requestFocus();
            Toast.makeText(getApplicationContext(), "Contraseña no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (us_contrasena.getText().toString().length() < 6){
            us_contrasena.requestFocus();
            Toast.makeText(getApplicationContext(), "La contraseña es muy corta", Toast.LENGTH_SHORT).show();

        } else {
            registrarUsuario();
        }

    }

    private void registrarUsuario() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE,us_nombres.getText().toString());
        values.put(Constantes.CAMPO_APELLIDO,us_apellidos.getText().toString());
        values.put(Constantes.CAMPO_CORREO,us_correo.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD, us_ciudad.getText().toString());
        values.put(Constantes.CAMPO_USER,us_usuario.getText().toString());
        values.put(Constantes.CAMPO_CONTRASENA,us_contrasena.getText().toString());

        db.insert(Constantes.TABLA_USUARIO,Constantes.CAMPO_ID,values);

        Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_SHORT).show();
        db.close();

        startActivity(new Intent(Crear_usuario.this, Login.class));
        finish();

    }
}


