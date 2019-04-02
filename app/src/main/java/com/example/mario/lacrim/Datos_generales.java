package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

public class Datos_generales extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText ed_nombre, ed_fecha_nacimiento, ed_lugar_nacimiento, ed_sexo, ed_color, ed_microship, ed_criador, ed_Tipo, ed_andar,ed_propietario;
    Button bt_actualizar;
    String id_equino;
    String interfaz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_generales);

        ed_nombre = findViewById(R.id.ed_nombre);
        ed_fecha_nacimiento = findViewById(R.id.ed_fecha_nacimiento);
        ed_lugar_nacimiento = findViewById(R.id.ed_lugar_nacimiento);
        ed_sexo = findViewById(R.id.ed_sexo);
        ed_color = findViewById(R.id.ed_color);
        ed_microship = findViewById(R.id.ed_microship);
        ed_criador = findViewById(R.id.ed_criador);
        ed_Tipo = findViewById(R.id.ed_Tipo);
        ed_andar = findViewById(R.id.ed_andar);
        ed_propietario = findViewById(R.id.ed_propietario);
        bt_actualizar = findViewById(R.id.bt_actualizar);


        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

        consultar();

        if (interfaz.equalsIgnoreCase("2")){

            bt_actualizar.setVisibility(View.GONE);
            ed_nombre.setEnabled(false);
            ed_fecha_nacimiento.setEnabled(false);
            ed_lugar_nacimiento.setEnabled(false);
            ed_sexo.setEnabled(false);
            ed_color.setEnabled(false);
            ed_microship.setEnabled(false);
            ed_criador.setEnabled(false);
            ed_Tipo.setEnabled(false);
            ed_andar.setEnabled(false);
            ed_propietario.setEnabled(false);


        }

        bt_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizarEquino();

            }
        });


    }

    private void consultar() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={getIntent().getExtras().getString("id")};
        String[] campos={Constantes.CAMPO_NOMBRE_EQUINO,Constantes.CAMPO_FECHA_EQUINO,Constantes.CAMPO_LUGAR_EQUINO,Constantes.CAMPO_SEXO_EQUINO,
        Constantes.CAMPO_COLOR_EQUINO,Constantes.CAMPO_MICROCHIP_EQUINO,Constantes.CAMPO_CRIADOR_EQUINO,Constantes.CAMPO_TIPO_EQUINO,
        Constantes.CAMPO_ANDAR_EQUINO,Constantes.CAMPO_PROPIETARIO_EQUINO};

        try {
            Cursor cursor =db.query(Constantes.TABLA_EQUINO,campos,Constantes.CAMPO_ID_EQUINO+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            ed_nombre.setText(cursor.getString(0));
            ed_fecha_nacimiento.setText(cursor.getString(1));
            ed_lugar_nacimiento.setText(cursor.getString(2));
            ed_sexo.setText(cursor.getString(3));
            ed_color.setText(cursor.getString(4));
            ed_microship.setText(cursor.getString(5));
            ed_criador.setText(cursor.getString(6));
            ed_Tipo.setText(cursor.getString(7));
            ed_andar.setText(cursor.getString(8));
            ed_propietario.setText(cursor.getString(9));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();
        }

    }


    private void actualizarEquino() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={getIntent().getExtras().getString("id")};

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE_EQUINO,ed_nombre.getText().toString());
        values.put(Constantes.CAMPO_LUGAR_EQUINO,ed_lugar_nacimiento.getText().toString());
        values.put(Constantes.CAMPO_MICROCHIP_EQUINO,ed_microship.getText().toString());
        values.put(Constantes.CAMPO_CRIADOR_EQUINO,ed_criador.getText().toString());
        values.put(Constantes.CAMPO_PROPIETARIO_EQUINO,ed_propietario.getText().toString());

        db.update(Constantes.TABLA_EQUINO,values,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Equino actualizado",Toast.LENGTH_LONG).show();
        db.close();

        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        startActivity(i);
        finish();

    }

    public void onBackPressed() {
        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }

}
