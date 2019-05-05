package com.example.mario.lacrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View view;
    FloatingActionButton Fbutton;
    ArrayList<Equinos> ListarEquinos;
    TextView txt_us;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token;
    ImageButton perfil_usuario;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;



    public perfil() {
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
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());

        txt_us = view.findViewById(R.id.txt_usu);
        perfil_usuario = view.findViewById(R.id.perfil_usuario);


        tabLayout = view.findViewById(R.id.tablayout_id);
        viewPager = view.findViewById(R.id.viewpager_id);

        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.AddFragment(new FragmentEquinos(), "Equinos");
        adapter.AddFragment(new FragmentPesebreras(), "Pesebreras");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        cargarDatosToken();

        conn = new ConexionSQLiteHelper(getActivity(), "bd_equinos", null, 1);

        perfil_usuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PerfilUsuario.class));
            }
        });


        consultar_usuario();
        return view;
    }


    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
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
            Toast.makeText(getActivity(),"El usuario no existe",Toast.LENGTH_LONG).show();

        }


    }


}
