package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link buscar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link buscar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class buscar extends Fragment {

    EditText ed_buscar_equino;
    View view;
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista_buscar;
    String palabra_buscar;

    public buscar() {
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

        view =  inflater.inflate(R.layout.fragment_buscar, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());

        ed_buscar_equino = view.findViewById(R.id.ed_buscar_equino);
        R_lista_buscar =  view.findViewById(R.id.R_lista_buscar);
        R_lista_buscar.setLayoutManager(this.mLayoutManager);


        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);


        ed_buscar_equino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                palabra_buscar = String.valueOf(charSequence);

                if (palabra_buscar.isEmpty()){

                    vaciar_lista();

                }else{

                    consultar_equinos(palabra_buscar);
                    R_lista_buscar.setAdapter(new Adaptador_lista(ListarEquinos));


                }

            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return view;
    }



    private void consultar_equinos(String palabra_buscar) {


        SQLiteDatabase db=conn.getReadableDatabase();

        Equinos equino=null;

        Cursor cursor;

        ListarEquinos = new ArrayList<>();


            String[] parametros={palabra_buscar+"%"};

            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO+" WHERE "+Constantes.CAMPO_NOMBRE_EQUINO+" LIKE ?",parametros);





        while (cursor.moveToNext()) {
            equino = new Equinos();
            equino.setId_equino(cursor.getString(0));
            equino.setNombre_equino(cursor.getString(1));
            equino.setSexo_equino(cursor.getString(4));
            equino.setAndar_equino(cursor.getString(9));
            equino.setColor_equino(cursor.getString(5));


            ListarEquinos.add(equino);

        }

        R_lista_buscar.setAdapter(new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), Detalle_equino.class);
                intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                intent.putExtra("interfaz","2");
                startActivity(intent);
                getActivity().finish();
            }
        }));
        cursor.close();




    }


    public void vaciar_lista(){

        ListarEquinos = new ArrayList<>();

        R_lista_buscar.setAdapter(new Adaptador_lista(ListarEquinos, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                Intent intent = new Intent(getActivity(), Detalle_equino.class);
                intent.putExtra("id",ListarEquinos.get(position).getId_equino());
                startActivity(intent);
                getActivity().finish();
            }
        }));


    }


}
