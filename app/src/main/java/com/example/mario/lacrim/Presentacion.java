package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Presentacion extends AppCompatActivity {

    String interfaz;
    String id_equino;
    Button btn_agregar_video;
    TextView txt_sin_video;
    VideoView video_presentacion;
    String videoBase64="";
    ConexionSQLiteHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        id_equino = getIntent().getExtras().getString("id");
        interfaz = getIntent().getExtras().getString("interfaz");

        video_presentacion = findViewById(R.id.video_presentacion);
        txt_sin_video = findViewById(R.id.txt_sin_video);
        btn_agregar_video = findViewById(R.id.btn_agregar_video);

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);



        if (interfaz.equalsIgnoreCase("2")){

            btn_agregar_video.setVisibility(View.GONE);

        }


        btn_agregar_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subir_video();

            }
        });

        cargarvideo();

    }



    private void cargarvideo() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={(getIntent().getExtras().getString("id"))};
        String[] campos={Constantes.CAMPO_VIDEO_EQUINO};
        //Cursor cursor;

        try {

            Cursor cursor =db.query(Constantes.TABLA_EQUINO,campos,Constantes.CAMPO_ID_EQUINO+"=?",parametros,null,null,null);

            cursor.moveToFirst();

            if (!cursor.isNull(0)){

                video_presentacion.setVisibility(View.VISIBLE);
                txt_sin_video.setVisibility(View.GONE);

                String codbase64 = cursor.getString(0);

                Uri uri = Uri.parse(codbase64);

                video_presentacion.setVideoPath(String.valueOf(uri));

                video_presentacion.seekTo(80);


                final MediaController mediaController = new MediaController(getApplicationContext());
                video_presentacion.setMediaController(mediaController);
                mediaController.setAnchorView(video_presentacion);

            }else {

                video_presentacion.setVisibility(View.GONE);
                txt_sin_video.setVisibility(View.VISIBLE);

            }

            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El equino no existe",Toast.LENGTH_LONG).show();

        }


    }


    public void subir_video(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent,"Seleccionar video"), 1);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){

            Uri path = data.getData();
            video_presentacion.setVideoPath(String.valueOf(path));


            if(data!=null) {
                Uri selectedVideoUri = data.getData();
                String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                Cursor cursor = managedQuery(selectedVideoUri, projection, null, null, null);

                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                // Setting the thumbnail of the video in to the image view
                InputStream inputStream = null;
// Converting the video in to the bytes
                try {
                    inputStream = getContentResolver().openInputStream(selectedVideoUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int len = 0;
                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String videoData = "";
                //Converting bytes into base64
                videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                String sinSaltoFinal2 = videoData.trim();
                videoBase64  = sinSaltoFinal2.replaceAll("\n", "");


                subirvideoequino();

            }
        }
    }

    public void subirvideoequino(){


            SQLiteDatabase db=conn.getWritableDatabase();
            String[] parametros={getIntent().getExtras().getString("id")};

            ContentValues values=new ContentValues();

            if (!videoBase64.equalsIgnoreCase("")){

                values.put(Constantes.CAMPO_VIDEO_EQUINO, videoBase64);

            }


            db.update(Constantes.TABLA_EQUINO,values,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Video actualizado",Toast.LENGTH_LONG).show();
            db.close();

            cargarvideo();

            //Intent i = new Intent(this, Detalle_equino.class);
            //i.putExtra("id",id_equino);
            //i.putExtra("interfaz",interfaz);
            //startActivity(i);
            //finish();

        }



    public void onBackPressed() {
        Intent i = new Intent(this, Detalle_equino.class);
        i.putExtra("id",id_equino);
        i.putExtra("interfaz",interfaz);
        startActivity(i);
        finish();
    }

}
