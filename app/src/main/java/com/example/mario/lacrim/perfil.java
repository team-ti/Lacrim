package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Utilidades.Constantes;


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
    ConexionSQLiteHelper conn;
    EditText ed_nombres, ed_apellidos, ed_correo, ed_ciudad, ed_usuario, ed_password;
    Button btn_actualizarus;
    View view;
    String token;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    private RecyclerView.LayoutManager mLayoutManager;



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
        view =  inflater.inflate(R.layout.fragment_perfil, container, false);
        ed_nombres = view.findViewById(R.id.ed_us_nombres);
        ed_apellidos = view.findViewById(R.id.ed_us_apellidos);
        ed_correo = view.findViewById(R.id.ed_us_correo);
        ed_ciudad = view.findViewById(R.id.ed_us_ciudad);
        ed_usuario = view.findViewById(R.id.ed_us_usuario);
        ed_password = view.findViewById(R.id.ed_us_contrasena);
        btn_actualizarus = view.findViewById(R.id.btn_actualizarus);

        cargarDatosToken();

        conn=new ConexionSQLiteHelper(getActivity(),"bd_equinos",null,1);

        consultar();

        btn_actualizarus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               actualizarUsuario();

            }
        });
        return view;
    }


    public void cargarDatosToken() {
        token = getActivity().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    private void consultar() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {token};
        String[] campos={Constantes.CAMPO_NOMBRE,Constantes.CAMPO_APELLIDO,Constantes.CAMPO_CORREO,Constantes.CAMPO_CIUDAD,
                Constantes.CAMPO_USER,Constantes.CAMPO_CONTRASENA};

        try {
            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);
            cursor.moveToFirst();

            ed_nombres.setText(cursor.getString(0));
            ed_apellidos.setText(cursor.getString(1));
            ed_correo.setText(cursor.getString(2));
            ed_ciudad.setText(cursor.getString(3));
            ed_usuario.setText(cursor.getString(4));
            ed_password.setText(cursor.getString(5));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getActivity(),"El usuario no existe",Toast.LENGTH_LONG).show();
        }

    }

    private void actualizarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NOMBRE,ed_nombres.getText().toString());
        values.put(Constantes.CAMPO_APELLIDO,ed_apellidos.getText().toString());
        values.put(Constantes.CAMPO_CORREO,ed_correo.getText().toString());
        values.put(Constantes.CAMPO_CIUDAD, ed_ciudad.getText().toString());
        values.put(Constantes.CAMPO_USER,ed_usuario.getText().toString().trim());
        values.put(Constantes.CAMPO_CONTRASENA,ed_password.getText().toString().trim());
        String[] parametros= {token};

        db.update(Constantes.TABLA_USUARIO, values, Constantes.CAMPO_ID+"=?",parametros);

        Toast.makeText(getActivity(),"Usuario actualizado",Toast.LENGTH_SHORT).show();
        db.close();



    }

}
