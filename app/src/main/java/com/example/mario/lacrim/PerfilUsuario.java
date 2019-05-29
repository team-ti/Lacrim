package com.example.mario.lacrim;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuario extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    TextView txt_us;
    CircleImageView img_foto_perfil;
    EditText ed_nombres, ed_apellidos, ed_ciudad;
    Button btn_actualizarus;
    View view;
    String token;
    String avatarBase64="";
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    Fragment fragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        img_foto_perfil = findViewById(R.id.img_foto_perfil);
        ed_nombres = findViewById(R.id.ed_us_nombres);
        ed_apellidos = findViewById(R.id.ed_us_apellidos);
        ed_ciudad = findViewById(R.id.ed_us_ciudad);
        txt_us = findViewById(R.id.txt_usu);
        btn_actualizarus = findViewById(R.id.btn_actualizarus);

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);


        consultar();
        consultar_usuario();


        img_foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"), 1);


            }
        });


        btn_actualizarus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();

            }
        });
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void consultar() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {token};
        String[] campos={Constantes.CAMPO_NOMBRE,Constantes.CAMPO_APELLIDO,Constantes.CAMPO_CIUDAD,Constantes.CAMPO_AVATAR};

        try {
            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            ed_nombres.setText(cursor.getString(0));
            ed_apellidos.setText(cursor.getString(1));
            ed_ciudad.setText(cursor.getString(2));

            if (!cursor.isNull(3)){

                String codbase64 = cursor.getString(3);

                byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                img_foto_perfil.setImageBitmap(img);

            }

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_LONG).show();
        }

    }

    private void validarcampos() {
        if (ed_nombres == null || ed_nombres.getText().toString().equals("")) {
            ed_nombres.requestFocus();
            Toast.makeText(getApplicationContext(), "Nombres no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (ed_apellidos == null || ed_apellidos.getText().toString().equals("")) {
            ed_apellidos.requestFocus();
            Toast.makeText(getApplicationContext(), "Apellidos no puede estar vacio", Toast.LENGTH_SHORT).show();

        } else if (ed_ciudad == null || ed_ciudad.getText().toString().equals("")) {
            ed_ciudad.requestFocus();
            Toast.makeText(getApplicationContext(), "Ciudad no puede estar vacio", Toast.LENGTH_SHORT).show();
        }else {
            actualizarUsuario();
        }
    }

    private void actualizarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE,ed_nombres.getText().toString());
        values.put(Constantes.CAMPO_APELLIDO,ed_apellidos.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD, ed_ciudad.getText().toString());

        if (!avatarBase64.equalsIgnoreCase("")){

            values.put(Constantes.CAMPO_AVATAR, avatarBase64);

        }


        String[] parametros= {token};

        db.update(Constantes.TABLA_USUARIO, values, Constantes.CAMPO_ID+"=?",parametros);

        Toast.makeText(getApplicationContext(),"Usuario actualizado",Toast.LENGTH_SHORT).show();
        db.close();
    }


    private void consultar_usuario() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={token};
        String[] campos={Constantes.CAMPO_USER};
        //Cursor cursor;

        try {

            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);

            cursor.moveToFirst();

            //txt_detalle_equino.setText(cursor.getString(1));
            txt_us.setText(cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_USER)));

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_LONG).show();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){

            Uri path = data.getData();
            img_foto_perfil.setImageURI(path);

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
