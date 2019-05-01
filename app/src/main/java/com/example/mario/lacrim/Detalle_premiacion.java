package com.example.mario.lacrim;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

public class Detalle_premiacion extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    EditText ed_nombre_detalle_premio, ed_fecha_detalle_premio, ed_descripcion_detalle_premio;
    String id_equino;
    String interfaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_premiacion);

        ed_nombre_detalle_premio = findViewById(R.id.ed_nombre_detalle_premio);
        ed_fecha_detalle_premio = findViewById(R.id.ed_fecha_detalle_premio);
        ed_descripcion_detalle_premio = findViewById(R.id.ed_descripcion_detalle_premio);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

        consultar_premio();

    }

    private void consultar_premio() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={getIntent().getExtras().getString("id")};
        String[] campos={Constantes.CAMPO_NOMBRE_PREMIO,Constantes.CAMPO_FECHA_PREMIO,Constantes.CAMPO_DESCRIPCION_PREMIO};

        try {
            Cursor cursor =db.query(Constantes.TABLA_PREMIOS,campos,Constantes.CAMPO_ID_EQUINO_PREMIO+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            ed_nombre_detalle_premio.setText(cursor.getString(0));
            ed_fecha_detalle_premio.setText(cursor.getString(1));
            ed_descripcion_detalle_premio.setText(cursor.getString(2));


            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();
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
