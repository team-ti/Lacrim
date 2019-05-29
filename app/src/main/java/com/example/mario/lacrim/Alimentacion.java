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
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Alimentacion extends AppCompatActivity {

    ArrayList<Alimentos> ListarAlimentos;
    ConexionSQLiteHelper conn;
    String id_equino;
    CircleImageView img_foto_perfil_equino_alimentacion;
    RecyclerView R_lista_alimentacion;
    TextView txt_detalle_equino_alimento;
    FloatingActionButton Fbutton_alimentos;
    private RecyclerView.LayoutManager mLayoutManager;
    String interfaz;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentacion);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

        img_foto_perfil_equino_alimentacion = findViewById(R.id.img_foto_perfil_equino_alimentacion);
        R_lista_alimentacion = findViewById(R.id.R_lista_alimentacion);
        mLayoutManager = new LinearLayoutManager(this);
        txt_detalle_equino_alimento = findViewById(R.id.txt_detalle_equino_alimento);
        Fbutton_alimentos = findViewById(R.id.Fbutton_alimentos);
        R_lista_alimentacion.setLayoutManager(this.mLayoutManager);

        if (interfaz.equalsIgnoreCase("2")){

            Fbutton_alimentos.setVisibility(View.GONE);

        }


        R_lista_alimentacion.setAdapter(new Adaptador_lista_alimentacion(ListarAlimentos));

        consultar_equino();
        consultarListaAlimentacion();

        Fbutton_alimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Crear_alimento_equino.class);
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

            txt_detalle_equino_alimento.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_NOMBRE_EQUINO)));

            if (!cursor.isNull(1)){

                String codbase64 = cursor.getString(1);

                byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                img_foto_perfil_equino_alimentacion.setImageBitmap(img);

            }

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();

        }


    }

    private void consultarListaAlimentacion() {


        ListarAlimentos = new ArrayList<>();

        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={(getIntent().getExtras().getString("id"))};
        Alimentos alimento=null;

        Cursor cursor;


        cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ALIMENTOS + " WHERE " +Constantes.CAMPO_ID_EQUINO_ALIMENTO+"=?",parametros);


        //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

        while (cursor.moveToNext()) {
            alimento = new Alimentos();

            alimento.setId_alimento(cursor.getString(0));
            alimento.setNombre(cursor.getString(1));
            alimento.setDescripcion(cursor.getString(2));
            alimento.setFecha_ali(cursor.getString(3));
            alimento.setId_equino(cursor.getString(4));


            ListarAlimentos.add(alimento);


        }

        R_lista_alimentacion.setAdapter(new Adaptador_lista_alimentacion(ListarAlimentos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getApplicationContext(), Detalle_alimentacion.class);
                intent.putExtra("id",ListarAlimentos.get(position).getId_alimento());
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
