package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

public class DatosPesebrera extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText txt_nombre, txt_encargado, txt_ciudad, txt_telefono;
    Button btn_actualizar;
    String id_pes;
    String interfaz;

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
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

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
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={getIntent().getExtras().getString("id")};
        String[] campos={Constantes.CAMPO_NOMBRE_PESEBRERA,Constantes.CAMPO_ENCARGADO_PESEBRERA,Constantes.CAMPO_CIUDAD_PESEBRERA,Constantes.CAMPO_TELEFONO_PESEBRERA
                };

        try {
            Cursor cursor =db.query(Constantes.TABLA_PESEBRERA,campos,Constantes.CAMPO_ID_PESEBRERA+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            txt_nombre.setText(cursor.getString(0));
            txt_encargado.setText(cursor.getString(1));
            txt_ciudad.setText(cursor.getString(2));
            txt_telefono.setText(cursor.getString(3));


            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"La pesebrera no existe",Toast.LENGTH_LONG).show();
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
            actualizar_pesebrera();
        }
    }

    private void actualizar_pesebrera() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={getIntent().getExtras().getString("id")};

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_PESEBRERA,txt_nombre.getText().toString());
        values.put(Constantes.CAMPO_ENCARGADO_PESEBRERA,txt_encargado.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD_PESEBRERA,txt_ciudad.getText().toString());
        values.put(Constantes.CAMPO_TELEFONO_PESEBRERA,txt_telefono.getText().toString());

        db.update(Constantes.TABLA_PESEBRERA,values,Constantes.CAMPO_ID_PESEBRERA+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Pesebrera actualizada",Toast.LENGTH_LONG).show();
        db.close();



    }

    public void onBackPressed() {
        Intent i = new Intent(this, DetallePesebrera.class);
        i.putExtra("id",id_pes);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }
}
