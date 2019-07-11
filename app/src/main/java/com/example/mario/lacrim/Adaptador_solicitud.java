package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.mario.lacrim.Entidades.VolleySingleton;
import com.example.mario.lacrim.Utilidades.Constantes;
import com.example.mario.lacrim.Entidades.Solicitudes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/////////

public class Adaptador_solicitud extends RecyclerView.Adapter<Adaptador_solicitud.MyViewHolder> implements View.OnClickListener {

    ArrayList<Solicitudes> sl;
    ConexionSQLiteHelper conn;
    int id_equino;
    int id_pesebrera;
    int id_solicitud;
    private Context mContext;
    private View.OnClickListener listener;
    String avatar;


    public Adaptador_solicitud(Context mContext, ArrayList<Solicitudes> sl) {
        this.mContext = mContext;
        this.sl = sl;
    }

    @NonNull
    @Override
    public Adaptador_solicitud.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_solicitud, viewGroup, false);
        view.setOnClickListener(this);
        return new Adaptador_solicitud.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adaptador_solicitud.MyViewHolder myViewHolder, final int i) {

        String nombre = sl.get(i).getNombre_solicitud() + " te ha enviado una solicitud";

        myViewHolder.tv_nombre_notificacion.setText(nombre);


        id_equino = sl.get(i).getId_equino();
        id_pesebrera = sl.get(i).getId_pesebrera();
        id_solicitud = sl.get(i).getId_solicitud();
        String avatar = sl.get(i).getAvatar();

        if (!avatar.equalsIgnoreCase("")) {

            String codbase64 = avatar;

            byte[] decodedString = Base64.decode(codbase64, Base64.DEFAULT);
            Bitmap img = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            myViewHolder.img_perfil_notificacion.setImageBitmap(img);

        }


        myViewHolder.img_perfil_notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Detalle_equino.class);
                String idString;
                idString = String.valueOf(sl.get(i).getId_equino());
                intent.putExtra("id", idString);
                intent.putExtra("interfaz", "2");
                mContext.startActivity(intent);
                //    getActivity().finish();

            }
        });


        myViewHolder.bt_aceptar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewHolder.bt_aceptar_solicitud.setVisibility(View.GONE);
                myViewHolder.bt_rechazar_solicitud.setVisibility(View.GONE);
                myViewHolder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                myViewHolder.texto_aceptar_rechazar.setText("Solicitud aceptada");

                insertar_aceptar_solicitud();

            }
        });


        myViewHolder.bt_rechazar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myViewHolder.bt_aceptar_solicitud.setVisibility(View.GONE);
                myViewHolder.bt_rechazar_solicitud.setVisibility(View.GONE);
                myViewHolder.texto_aceptar_rechazar.setVisibility(View.VISIBLE);
                myViewHolder.texto_aceptar_rechazar.setText("Solicitud rechazada");

                insertar_rechazar_solicitud();

            }
        });


    }

    private void insertar_aceptar_solicitud() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_equino", String.valueOf(id_equino));
        map.put("id_pesebrera", String.valueOf(id_pesebrera));
        map.put("id_solicitud", String.valueOf(id_solicitud));


        AceptarSolicitud(map);


    }

    public void AceptarSolicitud(HashMap<String, String> map) {
        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(mContext).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.PUT,
                        mContext.getString(R.string.url_server) + "solicitud/aceptar_solicitudes",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_aceptar(response);
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
    private void procesarRespuesta_aceptar(JSONObject response) {

        try {
            String cod = response.getString("cod");


            switch (cod) {
                case "1":
                    Toast.makeText(mContext, "Solicitud aceptada", Toast.LENGTH_SHORT).show();

                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertar_rechazar_solicitud() {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_solicitud", String.valueOf(id_solicitud));
        RechazarSolicitud(map);

    }


    public void RechazarSolicitud(HashMap<String, String> map) {

        JSONObject miObjetoJSON = new JSONObject(map);


        VolleySingleton.getInstance(mContext).addToRequestQueue(

                new JsonObjectRequest(
                        Request.Method.POST,
                        mContext.getString(R.string.url_server) + "solicitud/rechazar_solicitudes",
                        miObjetoJSON,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta_rechazar(response);
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

    private void procesarRespuesta_rechazar(JSONObject response) {

        try {
            String cod = response.getString("cod");


            switch (cod) {
                case "1":
                    Toast.makeText(mContext, "Solicitud rechazada", Toast.LENGTH_SHORT).show();

                    break;

                case "0":

                    //mensaje el correo ya existe
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String cargaravatar() {

        try {

            RequestQueue queue = Volley.newRequestQueue(mContext);
            String url = mContext.getResources().getString(R.string.url_server) + "equino/obtener_equino_info/" + id_equino;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {

                                    avatar = data.getJSONObject(i).getString("avatar_equino");

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
        return avatar;
    }


    @Override
    public int getItemCount() {
        return sl.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {

        if (listener!=null){
            listener.onClick(view);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_nombre_notificacion;
        public TextView texto_aceptar_rechazar;
        public Button bt_aceptar_solicitud;
        public Button bt_rechazar_solicitud;
        public ImageView img_perfil_notificacion;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_nombre_notificacion = itemView.findViewById(R.id.txt_nombre_notificacion);
            bt_aceptar_solicitud = itemView.findViewById(R.id.bt_aceptar_solicitud);
            bt_rechazar_solicitud = itemView.findViewById(R.id.bt_rechazar_solicitud);
            texto_aceptar_rechazar = itemView.findViewById(R.id.texto_aceptar_rechazar);
            img_perfil_notificacion = itemView.findViewById(R.id.img_perfil_notificacion);
        }
    }
}




