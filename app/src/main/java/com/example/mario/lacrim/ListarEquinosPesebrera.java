package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

public class ListarEquinosPesebrera extends AppCompatActivity {
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token, id_pes, interfaz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_equinos_pesebrera);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        R_lista =  findViewById(R.id.R_lista);
        R_lista.setLayoutManager(this.mLayoutManager);

        cargarDatosToken();
        id_pes = getIntent().getExtras().getString("id");

        interfaz = getIntent().getExtras().getString("interfaz");

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);
        consultarLista();


    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {


        ListarEquinos = new ArrayList<>();

        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={id_pes};
        Equinos equino=null;

        Cursor cursor;


        cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO+ " WHERE " +Constantes.CAMPO_ID_PESEBRERA_EQUINO+ "=?",parametros);


        //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

        while (cursor.moveToNext()) {
            equino = new Equinos();

            equino.setId_equino(cursor.getString(0));
            equino.setNombre_equino(cursor.getString(1));
            equino.setSexo_equino(cursor.getString(4));
            equino.setAndar_equino(cursor.getString(9));
            equino.setColor_equino(cursor.getString(5));


            ListarEquinos.add(equino);


        }

        R_lista.setAdapter(new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getApplicationContext(), Detalle_equino.class);
                intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                intent.putExtra("interfaz","1");
                //int id = ListarEquinos.get(position).getId_equino();
                //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
                startActivity(intent);
                //    getActivity().finish();
            }
        }));

        cursor.close();


    }
}
