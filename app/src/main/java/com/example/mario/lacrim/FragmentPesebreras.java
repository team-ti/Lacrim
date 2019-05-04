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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Entidades.Pesebrera;
import com.example.mario.lacrim.Utilidades.Constantes;

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

        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);
        consultarLista();


        //R_lista.setAdapter(new Adaptador_lista_pesebrera(ListarPesebrera));

        adaptador_pese = new Adaptador_lista_pesebrera(ListarPesebrera);
        R_lista.setAdapter(adaptador_pese);


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

        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {token};
        Pesebrera pese = null;

        Cursor cursor;

        try {
            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_PESEBRERA + " WHERE " + Constantes.CAMPO_ID_USUARIO_PESEBRERA + "=?", parametros);


            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

            while (cursor.moveToNext()) {
                pese = new Pesebrera();

                pese.setId_pes(cursor.getString(0));
                pese.setNombre_pes(cursor.getString(1));
                pese.setEncargado_pes(cursor.getString(2));
                pese.setCiudad_pes(cursor.getString(3));
                pese.setTelefono_pes(cursor.getString(4));


                ListarPesebrera.add(pese);


            }
            R_lista.setAdapter(new Adaptador_lista_pesebrera(ListarPesebrera, new RecyclerViewOnItemClickListener() {
                @Override
                public void onClick(View v, int position) {

                    Intent intent = new Intent(getActivity(), DetallePesebrera.class);
                    intent.putExtra("id", ListarPesebrera.get(position).getId_pes());
                    //Log.d("id", ListarPesebrera.get(position).getId_pes()+"");
                    intent.putExtra("interfaz", "1");
                    String id = ListarPesebrera.get(position).getId_pes();
                   // Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
                    startActivity(intent);

                }
            }));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();

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
