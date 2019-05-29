package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Datos_generales extends AppCompatActivity {

    ConexionSQLiteHelper conn;
    CircleImageView img_foto_perfil_equino_detalle_dato_general;
    EditText ed_nombre, ed_fecha_nacimiento, ed_lugar_nacimiento, ed_sexo, ed_color, ed_microship, ed_criador, ed_Tipo, ed_andar,ed_propietario;
    Button bt_actualizar;
    String id_equino;
    String interfaz;
    String avatarBase64="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_generales);

        img_foto_perfil_equino_detalle_dato_general = findViewById(R.id.img_foto_perfil_equino_detalle_dato_general);
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


        img_foto_perfil_equino_detalle_dato_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"), 1);


            }
        });


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
        Constantes.CAMPO_ANDAR_EQUINO,Constantes.CAMPO_PROPIETARIO_EQUINO,Constantes.CAMPO_AVATAR_EQUINO};

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


            if (!cursor.isNull(10)){

                String codbase64 = cursor.getString(10);

                byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                img_foto_perfil_equino_detalle_dato_general.setImageBitmap(img);

            }

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

        if (!avatarBase64.equalsIgnoreCase("")){

            values.put(Constantes.CAMPO_AVATAR, avatarBase64);

        }


        db.update(Constantes.TABLA_EQUINO,values,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Equino actualizado",Toast.LENGTH_LONG).show();
        db.close();

        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){

            Uri path = data.getData();
            img_foto_perfil_equino_detalle_dato_general.setImageURI(path);

            try {

                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                avatarBase64 = encodeImage(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        }

    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

}
