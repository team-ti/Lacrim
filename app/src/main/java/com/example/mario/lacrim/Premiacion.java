package com.example.mario.lacrim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Alimentos;
import com.example.mario.lacrim.Entidades.Premios;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Premiacion extends AppCompatActivity {

    ArrayList<Premios> ListarPremios;
    CircleImageView img_foto_perfil_equino_premiacion;
    ConexionSQLiteHelper conn;
    String id_equino;
    RecyclerView R_lista_premiacion;
    TextView txt_detalle_equino_premiacion;
    FloatingActionButton Fbutton_premiacion;
    private RecyclerView.LayoutManager mLayoutManager;
    String interfaz;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios);


        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);


        img_foto_perfil_equino_premiacion = findViewById(R.id.img_foto_perfil_equino_premiacion);
        R_lista_premiacion = findViewById(R.id.R_lista_premiacion);
        mLayoutManager = new LinearLayoutManager(this);
        txt_detalle_equino_premiacion = findViewById(R.id.txt_detalle_equino_premiacion);
        Fbutton_premiacion = findViewById(R.id.Fbutton_premiacion);
        R_lista_premiacion.setLayoutManager(this.mLayoutManager);

        if (interfaz.equalsIgnoreCase("2")){

            Fbutton_premiacion.setVisibility(View.GONE);

        }


        R_lista_premiacion.setAdapter(new Adaptador_lista_premiacion(ListarPremios));

        consultar_equino();
        consultarListaPremiacion();

        Fbutton_premiacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Crear_premio_equino.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();
            }
        });

    }


    private void consultar_equino() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={(getIntent().getExtras().getString("id"))};
        String[] campos={Constantes.CAMPO_NOMBRE_EQUINO,Constantes.CAMPO_AVATAR_EQUINO};
        //Cursor cursor;

        try {

            Cursor cursor =db.query(Constantes.TABLA_EQUINO,campos,Constantes.CAMPO_ID_EQUINO+"=?",parametros,null,null,null);
            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO+" WHERE "+Constantes.CAMPO_ID_EQUINO,parametros);

            cursor.moveToFirst();

            //txt_detalle_equino.setText(cursor.getString(1));

            txt_detalle_equino_premiacion.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_NOMBRE_EQUINO)));

            if (!cursor.isNull(1)){

                String codbase64 = cursor.getString(1);

                byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                img_foto_perfil_equino_premiacion.setImageBitmap(img);

            }


            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();

        }
    }


    private void consultarListaPremiacion() {


        ListarPremios = new ArrayList<>();

        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={(getIntent().getExtras().getString("id"))};
        Premios premio=null;

        Cursor cursor;


        cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_PREMIOS + " WHERE " +Constantes.CAMPO_ID_EQUINO_PREMIO+"=?",parametros);


        //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

        while (cursor.moveToNext()) {
            premio = new Premios();

            premio.setId_premio(cursor.getString(0));
            premio.setNombre(cursor.getString(1));
            premio.setDescripcion(cursor.getString(2));
            premio.setFecha_ali(cursor.getString(3));
            premio.setId_equino(cursor.getString(4));


            ListarPremios.add(premio);


        }

        R_lista_premiacion.setAdapter(new Adaptador_lista_premiacion(ListarPremios, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getApplicationContext(), Detalle_premiacion.class);
                intent.putExtra("id",ListarPremios.get(position).getId_premio());
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();
            }
        }));

        cursor.close();


    }



    public void onBackPressed() {
        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }


}
