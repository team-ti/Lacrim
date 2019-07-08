package com.example.mario.lacrim;

import android.app.ProgressDialog;
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
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuario extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    TextView txt_us;
    CircleImageView img_foto_perfil;
    EditText ed_nombres, ed_apellidos, ed_ciudad, ed_correo, ed_usuario;
    Button btn_actualizarus;
    View view;
    String token;
    String avatarBase64="";
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    //Fragment fragment;
    Uri path;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        img_foto_perfil = findViewById(R.id.img_foto_perfil);
        ed_nombres = findViewById(R.id.ed_us_nombres);
        ed_apellidos = findViewById(R.id.ed_us_apellidos);
        ed_ciudad = findViewById(R.id.ed_us_ciudad);
        ed_correo = findViewById(R.id.ed_us_correo);
       // ed_usuario = findViewById(R.id.ed_us_usuario);
        txt_us = findViewById(R.id.txt_usu);
        btn_actualizarus = findViewById(R.id.btn_actualizarus);

        progressDialog = ProgressDialog.show(this, "Cargando informacion", "Espere unos segundos");

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);


        consultar();


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
                progressDialog.show();
                //progressDialog = ProgressDialog.show(getApplicationContext(), "Cargando informacion", "Espere unos segundos");
                validarcampos();

            }
        });
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void consultar() {
        final String id_usuario = token;
        progressDialog = ProgressDialog.show(this, "Cargando datos usuario", "Espere unos segundos");

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"generic/usuario_info/"+id_usuario;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {

                                String nombres = "";
                                String apellidos = "";
                                String ciudad = "";
                                String usuario= "";
                                String avatar = "";
                                String correo = "";


                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    nombres = data.getJSONObject(i).getString("nombre");
                                    apellidos = data.getJSONObject(i).getString("apellido");
                                    ciudad = data.getJSONObject(i).getString("ciudad");
                                    usuario = data.getJSONObject(i).getString("usuario");
                                    avatar = data.getJSONObject(i).getString("avatar");
                                    correo  = data.getJSONObject(i).getString("correo");

                                }

                                ed_nombres.setText(nombres);
                                ed_apellidos.setText(apellidos);
                                ed_ciudad.setText(ciudad);
                                ed_correo.setText(correo);
                                txt_us.setText(usuario);


                                if (!avatar.equalsIgnoreCase("")){

                                    String codbase64 = avatar;

                                    byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                                    Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    img_foto_perfil.setImageBitmap(img);

                                }

                            } catch (JSONException e) {
                                progressDialog.dismiss();

                                e.printStackTrace();
                            }
                             progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

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
            insertar_usuario();
        }
    }

    private void insertar_usuario() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre", ed_nombres.getText().toString());
        map.put("apellido", ed_apellidos.getText().toString());
        map.put("ciudad", ed_apellidos.getText().toString());
        if (!avatarBase64.equalsIgnoreCase("")){
            map.put("avatar", avatarBase64);
        }else{
            map.put("avatar", "");
        }

        map.put("id_usuario", token);


        actualizarUsuario(map);


    }

    private void actualizarUsuario(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.PUT,
                        getResources().getString(R.string.url_server)+"generic/usuario_actualizar",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_actualizar(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "Error Volley: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void procesarRespuesta_actualizar(JSONObject response) {

        try {
            String cod = response.getString("cod");

            switch (cod) {
                case "1":
                    Toast.makeText(getApplicationContext(),"Usuario actualizado",Toast.LENGTH_SHORT).show();
                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();


                    break;
            }

            if (!avatarBase64.equalsIgnoreCase("")){

                //perfil fragment =  new perfil();
                //fragment.cambiarImagen(path);

                //perfil fragment = (perfil) getSupportFragmentManager().findFragmentById(R.id.per);
                //fragment.cambiarImagen(path);

                //((perfil) g).cambiarImagen(path);




            }

            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }






    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){

            path = data.getData();

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
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("vista","hola");
        startActivity(i);
        finish();
    }

}
