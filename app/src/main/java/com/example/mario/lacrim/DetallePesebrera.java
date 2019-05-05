package com.example.mario.lacrim;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

public class DetallePesebrera extends AppCompatActivity {

    TextView txt_detalle_pes;
    String id_pesebrera;
    LinearLayout ln_datos_generales_pes, ln_lista_equinos, ln_solicitud;
    String interfaz_pes;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pesebrera);

        txt_detalle_pes = findViewById(R.id.txt_detalle_pes);
        ln_datos_generales_pes = findViewById(R.id.ln_datos_generales_pes);
        ln_lista_equinos = findViewById(R.id.ln_lista_equinos);
        ln_solicitud = findViewById(R.id.ln_solicitud);

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

        id_pesebrera = getIntent().getExtras().getString("id");
        interfaz_pes = getIntent().getExtras().getString("interfaz");


        ln_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SeleccionarEquino.class);

                intent.putExtra("id",id_pesebrera);
                Log.d("id_pes", ""+id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);

                startActivity(intent);
                finish();

            }
        });

        ln_datos_generales_pes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DatosPesebrera.class);

                intent.putExtra("id",id_pesebrera);
                Log.d("id_pes", ""+id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);

                startActivity(intent);
                finish();

            }
        });

        ln_datos_generales_pes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DatosPesebrera.class);

                intent.putExtra("id",id_pesebrera);
                Log.d("id_pes", ""+id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);

                startActivity(intent);
                finish();

            }
        });

        ln_lista_equinos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Premiacion.class);
                intent.putExtra("id",id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);
                startActivity(intent);
                finish();

            }
        });

        consultar_pesebrera();

    }

    private void consultar_pesebrera() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={(getIntent().getExtras().getString("id"))};
        String[] campos={Constantes.CAMPO_NOMBRE_PESEBRERA};
        //Cursor cursor;

        try {

            Cursor cursor =db.query(Constantes.TABLA_PESEBRERA,campos,Constantes.CAMPO_ID_PESEBRERA+"=?",parametros,null,null,null);

            cursor.moveToFirst();

            //txt_detalle_equino.setText(cursor.getString(1));
            txt_detalle_pes.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_NOMBRE_PESEBRERA)));

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"La pesebrera no existe",Toast.LENGTH_LONG).show();

        }
    }


}
