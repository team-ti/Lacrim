package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.regex.Pattern;

public class PerfilUsuario extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    TextView txt_us;
    EditText ed_nombres, ed_apellidos, ed_ciudad;
    Button btn_actualizarus;
    View view;
    String token;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    Fragment fragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        ed_nombres = findViewById(R.id.ed_us_nombres);
        ed_apellidos = findViewById(R.id.ed_us_apellidos);
        ed_ciudad = findViewById(R.id.ed_us_ciudad);
        txt_us = findViewById(R.id.txt_usu);
        btn_actualizarus = findViewById(R.id.btn_actualizarus);

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);


        consultar();
        consultar_usuario();

        btn_actualizarus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void consultar() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {token};
        String[] campos={Constantes.CAMPO_NOMBRE,Constantes.CAMPO_APELLIDO,Constantes.CAMPO_CIUDAD};

        try {
            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            ed_nombres.setText(cursor.getString(0));
            ed_apellidos.setText(cursor.getString(1));
            ed_ciudad.setText(cursor.getString(2));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_LONG).show();
        }

    }

    private void validarcampos() {
        if (ed_nombres == null || ed_nombres.getText().toString().equals("")) {
            ed_nombres.requestFocus();
            Toast.makeText(getApplicationContext(), "Nombres no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (ed_apellidos == null || ed_apellidos.getText().toString().equals("")) {
            ed_apellidos.requestFocus();
            Toast.makeText(getApplicationContext(), "Apellidos no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (ed_ciudad == null || ed_ciudad.getText().toString().equals("")) {
            ed_ciudad.requestFocus();
            Toast.makeText(getApplicationContext(), "Ciudad no puede estar vacio", Toast.LENGTH_SHORT).show();
        }else {
            actualizarUsuario();
        }
    }

    private void actualizarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE,ed_nombres.getText().toString());
        values.put(Constantes.CAMPO_APELLIDO,ed_apellidos.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD, ed_ciudad.getText().toString());
        String[] parametros= {token};

        db.update(Constantes.TABLA_USUARIO, values, Constantes.CAMPO_ID+"=?",parametros);

        Toast.makeText(getApplicationContext(),"Usuario actualizado",Toast.LENGTH_SHORT).show();
        db.close();
    }

    private void consultar_usuario() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={token};
        String[] campos={Constantes.CAMPO_USER};
        //Cursor cursor;

        try {

            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);

            cursor.moveToFirst();

            //txt_detalle_equino.setText(cursor.getString(1));
            txt_us.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_USER)));

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_LONG).show();

        }


    }

}
