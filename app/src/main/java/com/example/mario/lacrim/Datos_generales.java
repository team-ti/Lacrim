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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

                insertar_equino();

            }
        });


    }

    private void consultar() {

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url =getResources().getString(R.string.url_server)+"equino/obtener_equino_info/"+id_equino;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                String nombre = "";
                                String fecha_nacimiento = "";
                                String lugar_nacimiento = "";
                                String sexo= "";
                                String color = "";
                                String microship = "";
                                String criador = "";
                                String tipo = "";
                                String andar = "";
                                String propietario = "";
                                String avatar = "";



                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    nombre = data.getJSONObject(i).getString("nombre_equino");
                                    fecha_nacimiento = data.getJSONObject(i).getString("fecha_equino");
                                    lugar_nacimiento = data.getJSONObject(i).getString("lugar_equino");
                                    sexo = data.getJSONObject(i).getString("sexo_equino");
                                    color = data.getJSONObject(i).getString("color_equino");
                                    microship = data.getJSONObject(i).getString("microship_equino");
                                    criador = data.getJSONObject(i).getString("criador_equino");
                                    tipo = data.getJSONObject(i).getString("tipo_equino");
                                    andar = data.getJSONObject(i).getString("andar_equino");
                                    propietario = data.getJSONObject(i).getString("propietario_equino");
                                    avatar = data.getJSONObject(i).getString("avatar_equino");

                                }

                                ed_nombre.setText(nombre);
                                ed_fecha_nacimiento.setText(fecha_nacimiento);
                                ed_lugar_nacimiento.setText(lugar_nacimiento);
                                ed_sexo.setText(sexo);
                                ed_color.setText(color);
                                ed_microship.setText(microship);
                                ed_criador.setText(criador);
                                ed_Tipo.setText(tipo);
                                ed_andar.setText(andar);
                                ed_propietario.setText(propietario);

                                if (!avatar.equalsIgnoreCase("")){

                                    String codbase64 = avatar;

                                    byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
                                    Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    img_foto_perfil_equino_detalle_dato_general.setImageBitmap(img);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // progressDialog.dismiss();
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {

        }
    }



    private void insertar_equino() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre_equino", ed_nombre.getText().toString());
        map.put("lugar_equino", ed_lugar_nacimiento.getText().toString());
        map.put("microship_equino", ed_microship.getText().toString());
        map.put("criador_equino", ed_criador.getText().toString());
        map.put("propietario_equino", ed_propietario.getText().toString());
        map.put("id_equino", id_equino);
        if (!avatarBase64.equalsIgnoreCase("")){
            map.put("avatar_equino", avatarBase64);
        }else{
            map.put("avatar_equino", "");
        }

        actualizarEquino(map);


    }

    private void actualizarEquino(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.PUT,
                        getResources().getString(R.string.url_server)+"equino/equino_actualizar",
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void procesarRespuesta_actualizar(JSONObject response) {

        try {
           String cod = response.getString("cod");


            switch (cod) {
                case "1":
                    Toast.makeText(getApplicationContext(), "Equino actualizado", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, Detalle_equino.class);
                    i.putExtra("id", id_equino);
                    i.putExtra("interfaz", interfaz);
                    startActivity(i);
                    finish();
                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

}
