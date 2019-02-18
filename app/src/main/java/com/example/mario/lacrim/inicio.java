package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link inicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicio extends Fragment {


    FloatingActionButton Fbutton;
    View view;
    ArrayList<Equinos> ListarEquinos = new ArrayList();
    private RecyclerView.LayoutManager mLayoutManager;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;

    public inicio() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_inicio, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        Fbutton =  view.findViewById(R.id.Fbutton);
        R_lista =  view.findViewById(R.id.R_lista);
        R_lista.setLayoutManager(this.mLayoutManager);

        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);
        consultarLista();


        R_lista.setAdapter(new Adaptador_lista(ListarEquinos));

        Fbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Crear_equino.class));
                getActivity().finish();
            }
        });

        R_lista.setAdapter(new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getActivity(), Detalle_equino.class);
                intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                startActivity(intent);
                getActivity().finish();
            }
        }));

        return view;
    }


    private void consultarLista() {


        SQLiteDatabase db=conn.getReadableDatabase();

        Equinos equino=null;

        Cursor cursor;


            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO,null);


        //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);

        while (cursor.moveToNext()) {
            equino = new Equinos();

            equino.setNombre_equino(cursor.getString(1));
            equino.setSexo_equino(cursor.getString(4));
            equino.setAndar_equino(cursor.getString(9));
            equino.setColor_equino(cursor.getString(5));

            ListarEquinos.add(equino);


        }
        cursor.close();


    }


}
