package com.example.mario.lacrim;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.lacrim.Database.ConexionSQLiteHelper;
import com.example.mario.lacrim.Entidades.Equinos;
import com.example.mario.lacrim.Utilidades.Constantes;

import java.util.ArrayList;

public class SeleccionarEquino extends AppCompatActivity {
    ArrayList<Equinos> ListarEquinos;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView txt_sin_equinos;
    public static final String dataUserCache = "dataUser";
    private static final int modo_private = Context.MODE_PRIVATE;
    ConexionSQLiteHelper conn;
    RecyclerView R_lista;
    String token, equino_id, id_pes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_equino);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        txt_sin_equinos = findViewById(R.id.txt_sin_equinos);
        R_lista =  findViewById(R.id.R_lista_selecciona_equino);
        R_lista.setLayoutManager(this.mLayoutManager);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.5));

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cargarDatosToken();

        id_pes = getIntent().getExtras().getString("id");

        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_equinos",null,1);
        consultarLista();


        R_lista.setAdapter(new Adaptador_seleccionar_equino(ListarEquinos));
    }

    public void cargarDatosToken() {
        token = getApplicationContext().getSharedPreferences(dataUserCache,modo_private).getString("access_token", "no hay info");
    }

    boolean consultarEquinoSolicitud(String id) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);
        Log.d("hola", "consultarEquinoSolicitud: "+id);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor, cursor2;
        String id_equino = id;
        String[] columns = {Constantes.CAMPO_ID_SOLICITUD};
        String selection = Constantes.CAMPO_ID_EQUINO_SOLICITUD + " = ?";
        String[] selectionArgs = {id_equino};
        String tableName = Constantes.TABLA_SOLICITUD;
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Log.d(" existe 1", " existe: ");

            return true;

        }else {

            String[] columns2 = {Constantes.CAMPO_ID_PESEBRERA_EQUINO};
            String selection2 = Constantes.CAMPO_ID_EQUINO + " = ?";
            String[] selectionArgs2 = {id_equino};
            String tableName2 = Constantes.TABLA_EQUINO;
            cursor2 = db.query(tableName2, columns2, selection2, selectionArgs2, null, null, null);
            cursor2.moveToFirst();
            String pes_eq = cursor2.getString(0);
            Log.d("pes_eq", pes_eq);
            if ( Integer.parseInt(pes_eq) != 0){
                Log.d(" xiste2", " existe: ");

                return true;
            }else{
                Log.d("no existe", "no existe: ");
                return false;
            }
        }


    }

    boolean consultarPesebreraSolicitud() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor;
        String[] columns = {Constantes.CAMPO_ID_PESEBRERA};
        String selection = Constantes.CAMPO_ID_PESEBRERA + " = ? AND " + Constantes.CAMPO_ID_USUARIO_PESEBRERA + " = ? "  ;
        String[] selectionArgs = {id_pes,token};
        String tableName = Constantes.TABLA_PESEBRERA;
        cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst() == true) {
            return true;
        }else{
            return false;
        }
    }

    private void consultarLista() {


        ListarEquinos = new ArrayList<>();

        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {token};

        Equinos equino = null;

         Cursor cursor,cursor1;


        try {
            cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_EQUINO + " WHERE " + Constantes.CAMPO_ID_USUARIO_EQUINO + "=?", parametros);

            //cursor = db.rawQuery("SELECT * FROM " + Constantes.TABLA_ACTIVIDAD+busqueda+"AND"+item_bus,null);
        int c=0;

            if (cursor.getCount()>0) {
                while (cursor.moveToNext()) {

                    if (!consultarEquinoSolicitud(cursor.getString(0))) {
                        equino = new Equinos();

                        equino.setId_equino(cursor.getString(0));
                        equino.setNombre_equino(cursor.getString(1));
                        equino.setAndar_equino(cursor.getString(9));
                        ListarEquinos.add(equino);

                        c=c+1;

                    }

                }

                if (c==0){

                    R_lista.setVisibility(View.GONE);
                    txt_sin_equinos.setVisibility(View.VISIBLE);
                }

            }else{

                R_lista.setVisibility(View.GONE);
                txt_sin_equinos.setVisibility(View.VISIBLE);

            }
            R_lista.setAdapter(new Adaptador_seleccionar_equino(ListarEquinos, new RecyclerViewOnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    equino_id = ListarEquinos.get(position).getId_equino();

                        String[] param_solicitud = {equino_id};
                        if (!consultarPesebreraSolicitud()) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SeleccionarEquino.this);
                            builder1.setMessage("¿Quieres enviar solicitud con este equino? ");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Si",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            enviarSolicitud();

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SeleccionarEquino.this);
                            builder1.setMessage("¿Quieres agregar este equino a su pesebrera? ");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Si",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            agregarEquino();

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    /*Intent intent = new Intent(getApplicationContext(), Detalle_equino.class);
                    intent.putExtra("id", ListarEquinos.get(position).getId_equino());
                    startActivity(intent);*/

                }
            }));

            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

        }
    }


    private void agregarEquino() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros = {equino_id};

        ContentValues values = new ContentValues();
        values.put(Constantes.CAMPO_ID_PESEBRERA_EQUINO, id_pes);
        try {
            db.update(Constantes.TABLA_EQUINO,values,Constantes.CAMPO_ID_EQUINO+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Equino agregado",Toast.LENGTH_LONG).show();
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

        }

        finish();

    }

    private void enviarSolicitud(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_equinos", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        SQLiteDatabase db2 = conn.getReadableDatabase();
        String [] parametros = {token};
        String [] parametros2 = {id_pes};

        String[] campos={Constantes.CAMPO_NOMBRE};
        String[] campos2={Constantes.CAMPO_ID_USUARIO_PESEBRERA};

        Cursor cursor, cursor2;
        cursor =db2.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_ID+"=?",parametros,null,null,null);
        cursor.moveToFirst();
        cursor2 = db2.query(Constantes.TABLA_PESEBRERA,campos2,Constantes.CAMPO_ID_USUARIO_PESEBRERA+"=?",parametros2,null,null,null);
        cursor2.moveToFirst();

        String nombre_user;
        Integer user_receptor;
        nombre_user = cursor.getString(cursor.getColumnIndex(Constantes.CAMPO_NOMBRE));
        cursor.close();
        user_receptor = cursor2.getInt(cursor2.getColumnIndex(Constantes.CAMPO_ID_USUARIO_PESEBRERA));


        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_ID_EQUINO_SOLICITUD,equino_id);
        values.put(Constantes.CAMPO_ID_USER_SOLICITUD,token);
        values.put(Constantes.CAMPO_ID_PESEBRERA_SOLICITUD,id_pes);
        values.put(Constantes.CAMPO_NOMBRE_USER_SOLICITUD,nombre_user);
        values.put(Constantes.CAMPO_ID_USER_RECEPTOR_SOLICITUD,user_receptor);



        db.insert(Constantes.TABLA_SOLICITUD,Constantes.CAMPO_ID_SOLICITUD,values);

        Toast.makeText(getApplicationContext(),"Solicitud enviada",Toast.LENGTH_SHORT).show();
        db.close();
        db2.close();

        //startActivity(new Intent(Crear_equino.this, MainActivity.class));
        finish();

    }


}
