package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

public class SeleccionarEquino extends AppCompatActivity {
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView txt_sin_equinos;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_equino);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        txt_sin_equinos = findViewById(R.id.txt_sin_equinos);
        R_lista =  findViewById(R.id.R_lista_selecciona_equino);
        R_lista.setLayoutManager(this.mLayoutManager);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);
        consultarLista();


        R_lista.setAdapter(new Adaptador_seleccionar_equino(ListarEquinos));
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {


        ListarEquinos = new ArrayList<>();

        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {token};
        Equinos equino = null;

        Cursor cursor;

        try {
            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO + " WHERE " + Constantes.CAMPO_ID_USUARIO_EQUINO + "=?", parametros);


            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

            if (cursor.moveToNext()) {
                equino = new Equinos();

                equino.setId_equino(cursor.getString(0));
                equino.setNombre_equino(cursor.getString(1));
                equino.setAndar_equino(cursor.getString(9));


                ListarEquinos.add(equino);


            }else{

                R_lista.setVisibility(View.GONE);
                txt_sin_equinos.setVisibility(View.VISIBLE);

            }
            R_lista.setAdapter(new Adaptador_seleccionar_equino(ListarEquinos, new RecyclerViewOnItemClickListener() {
                @Override
                public void onClick(View v, int position) {

                    /*Intent intent = new Intent(getApplicationContext(), Detalle_equino.class);
                    intent.putExtra("id", ListarEquinos.get(position).getId_equino());
                    startActivity(intent);*/

                }
            }));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

        }
    }
}
