package com.example.mario.lacrim;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalle_equino extends AppCompatActivity {

    TextView txt_detalle_equino;
    String id_equino;
    LinearLayout ln_datos_generales, ln_premios, ln_alimentos,ln_videos;
    String interfaz;
    CircleImageView img_foto_perfil_equino_detalle;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_equino);

        img_foto_perfil_equino_detalle = findViewById(R.id.img_foto_perfil_equino_detalle);
        txt_detalle_equino = findViewById(R.id.txt_detalle_equino);
        ln_datos_generales = findViewById(R.id.ln_datos_generales);
        ln_premios = findViewById(R.id.ln_premios);
        ln_alimentos = findViewById(R.id.ln_alimentos);
        ln_videos = findViewById(R.id.ln_videos);

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");



        ln_datos_generales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Datos_generales.class);

                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);

                startActivity(intent);
                finish();

            }
        });

        ln_premios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Premiacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();

            }
        });

        ln_alimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Alimentacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();

            }
        });


        ln_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Presentacion.class);
                intent.putExtra("id",id_equino);
                intent.putExtra("interfaz",interfaz);
                startActivity(intent);
                finish();

            }
        });

        consultar_equino();


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
            txt_detalle_equino.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_NOMBRE_EQUINO)));

            if (!cursor.isNull(1)){

                String codbase64 = cursor.getString(1);

                byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                img_foto_perfil_equino_detalle.setImageBitmap(img);

            }

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();

        }


    }


  /*  public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }*/


}