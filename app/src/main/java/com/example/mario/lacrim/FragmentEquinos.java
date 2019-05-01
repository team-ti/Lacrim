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
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

public class FragmentEquinos extends Fragment {
    View view;
    FloatingActionButton btn_crear;
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token;


    public FragmentEquinos() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.equino_fragment, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        btn_crear =  view.findViewById(R.id.Fbutton_perfil);
        R_lista =  view.findViewById(R.id.R_lista_perfil);
        R_lista.setLayoutManager(this.mLayoutManager);

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);
        consultarLista();


        R_lista.setAdapter(new Adaptador_lista(ListarEquinos));

        btn_crear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Crear_equino.class));
                getActivity().finish();
            }
        });

        return view;

    }

    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }


    private void consultarLista() {


        ListarEquinos = new ArrayList<>();

        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {token};
        Equinos equino = null;

        Cursor cursor;

        try {
            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO + " WHERE " + Constantes.CAMPO_ID_USUARIO_EQUINO + "=?", parametros);


            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

            while (cursor.moveToNext()) {
                equino = new Equinos();

                equino.setId_equino(cursor.getString(0));
                equino.setNombre_equino(cursor.getString(1));
                equino.setSexo_equino(cursor.getString(4));
                equino.setAndar_equino(cursor.getString(9));
                equino.setColor_equino(cursor.getString(5));


                ListarEquinos.add(equino);


            }
            R_lista.setAdapter(new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
                @Override
                public void onClick(View v, int position) {

                    Intent intent = new Intent(getActivity(), Detalle_equino.class);
                    intent.putExtra("id", ListarEquinos.get(position).getId_equino());
                    intent.putExtra("interfaz", "1");

                    //int id = ListarEquinos.get(position).getId_equino();
                    //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
                    startActivity(intent);

                }
            }));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();

        }
    }
}
