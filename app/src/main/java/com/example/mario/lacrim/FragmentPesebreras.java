package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.Utilidades.Constantes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentPesebreras extends Fragment {
    View view;
    FloatingActionButton btn_crear;
    static ArrayList<Pesebrera> ListarPesebrera;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token;
    static  Adaptador_lista_pesebrera adaptador_pese;

    public FragmentPesebreras() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.pesebrera_fragment,container,false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        btn_crear =  view.findViewById(R.id.Fbutton_pes);
        R_lista =  view.findViewById(R.id.R_lista_pes);
        R_lista.setLayoutManager(this.mLayoutManager);

        cargarDatosToken();

        consultarLista();


        btn_crear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CrearPesebrera.class));
                //getActivity().finish();
            }
        });

        return view;
    }

    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {
        ListarPesebrera = new ArrayList<>();

        final String id_usuario = token;

        try {

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url =getResources().getString(R.string.url_server)+"pesebreras/obtener_pesebreras/"+id_usuario;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                String  id_pes="";
                                String nombre_pes = "";
                                String encargado_pes = "";
                                String ciudad_pes = "";
                                String telefono_pes = "";
                                String id_user = "";

                                JSONArray data = new JSONArray(response);


                                for (int i = 0; i < data.length(); i++) {
                                    id_pes = data.getJSONObject(i).getString("id_pes");
                                    nombre_pes = data.getJSONObject(i).getString("nombre_pes");
                                    encargado_pes = data.getJSONObject(i).getString("encargado_pes");
                                    ciudad_pes = data.getJSONObject(i).getString("ciudad_pes");
                                    telefono_pes= data.getJSONObject(i).getString("telefono_pes");
                                    id_user = data.getJSONObject(i).getString("id_user");

                                    Pesebrera pese = new Pesebrera();

                                    pese.setId_pes(id_pes);
                                    pese.setNombre_pes(nombre_pes);
                                    pese.setEncargado_pes(encargado_pes);
                                    pese.setCiudad_pes(ciudad_pes);
                                    pese.setTelefono_pes(telefono_pes);
                                    pese.setId_user_pes(id_user);

                                    ListarPesebrera.add(pese);
                                }
                                //procesarRespuesta(id, cod);

                                Adaptador_lista_pesebrera myAdapter = new Adaptador_lista_pesebrera(ListarPesebrera, new RecyclerViewOnItemClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent = new Intent(getActivity(), DetallePesebrera.class);
                                        intent.putExtra("id",ListarPesebrera.get(position).getId_pes());
                                        intent.putExtra("interfaz","1");
                                        intent.putExtra("id_user", ListarPesebrera.get(position).getId_user_pes());
                                        intent.putExtra("nombre_pes", ListarPesebrera.get(position).getNombre_pes());

                                        startActivity(intent);
                                    }
                                });

                                R_lista.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                                R_lista.setHasFixedSize(true);
                                R_lista.setAdapter(myAdapter);

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

    public void ChangeAdapterPesebrera(Pesebrera pese) {
        int position = 0;

       // for (int i = 0; i < lstPost.size(); i++) {

         //   if (lstPost.get(i).getId()==id){

                ListarPesebrera.add(pese);
                //adaptador_pese.notifyItemChanged(position, lstPost);
                adaptador_pese.notifyItemInserted(position);
                adaptador_pese.notifyDataSetChanged();

           // }
       // }
    }


}
