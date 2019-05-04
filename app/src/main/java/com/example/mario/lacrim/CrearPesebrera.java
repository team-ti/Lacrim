package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.Utilidades.Constantes;

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
            registrarPesebrera();
        }
    }

    private void registrarPesebrera() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_equinos",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_PESEBRERA,nombre.getText().toString());
        values.put(Constantes.CAMPO_ENCARGADO_PESEBRERA,encargado.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD_PESEBRERA,ciudad.getText().toString());
        values.put(Constantes.CAMPO_TELEFONO_PESEBRERA,telefono.getText().toString());
        values.put(Constantes.CAMPO_ID_USUARIO_PESEBRERA,token);



        long id = db.insert(Constantes.TABLA_PESEBRERA,Constantes.CAMPO_ID_PESEBRERA,values);



        Toast.makeText(getApplicationContext(),"Pesebrera resgistrada",Toast.LENGTH_SHORT).show();
        db.close();

        //startActivity(new Intent(CrearPesebrera.this, MainActivity.class));

        Pesebrera pese = null;

        pese = new Pesebrera();

        pese.setId_pes(String.valueOf(id));
        pese.setNombre_pes(nombre.getText().toString());
        pese.setEncargado_pes(encargado.getText().toString());
        pese.setCiudad_pes(ciudad.getText().toString());
        pese.setTelefono_pes(telefono.getText().toString());

        FragmentPesebreras fragmentPesebreras = new FragmentPesebreras();
        fragmentPesebreras.ChangeAdapterPesebrera(pese);

        finish();

    }


    public void onBackPressed() {
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        finish();
    }
}
