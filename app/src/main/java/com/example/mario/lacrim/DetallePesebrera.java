package com.example.mario.lacrim;

import android.content.Context;
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

    TextView txt_detalle_pes, text_solicitud;
    String id_pesebrera;
    LinearLayout ln_datos_generales_pes, ln_lista_equinos, ln_solicitud;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    String interfaz_pes;
    String token;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pesebrera);

        txt_detalle_pes = findViewById(R.id.txt_detalle_pes);
        ln_datos_generales_pes = findViewById(R.id.ln_datos_generales_pes);
        ln_lista_equinos = findViewById(R.id.ln_lista_equinos);
        ln_solicitud = findViewById(R.id.ln_solicitud);
        text_solicitud = findViewById(R.id.text_solicitud);

        cargarDatosToken();

        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_equinos", null, 1);

        id_pesebrera = getIntent().getExtras().getString("id");
        interfaz_pes = getIntent().getExtras().getString("interfaz");


        if (consultarPesebreraUser()){
            text_solicitud.setText("Agregar Equino");
        }

        ln_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SeleccionarEquino.class);

                intent.putExtra("id",id_pesebrera);
                Log.d("id_pes", ""+id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);

                startActivity(intent);
                //lfinish();

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

                Intent intent = new Intent(getApplicationContext(), ListarEquinosPesebrera.class);
                intent.putExtra("id",id_pesebrera);
                intent.putExtra("interfaz",interfaz_pes);
                startActivity(intent);
                finish();

            }
        });

        consultar_pesebrera();

    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    boolean consultarPesebreraUser() {
        Log.d("token", " "+token);
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor;
        String[] columns = {Constantes.CAMPO_ID_PESEBRERA};
        String selection = Constantes.CAMPO_ID_PESEBRERA + " = ? AND " + Constantes.CAMPO_ID_USUARIO_PESEBRERA + " = ? "  ;
        String[] selectionArgs = {id_pesebrera,token};
        String tableName = Constantes.TABLA_PESEBRERA;
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst() == true) {
            return true;
        }else{
            return false;
        }
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
