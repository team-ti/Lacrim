package com.example.mario.lacrim.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mario.lacrim.Utilidades.Constantes;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constantes.CREAR_TABLA_USUARIO);
        db.execSQL(Constantes.CREAR_TABLA_EQUINO);
        db.execSQL(Constantes.CREAR_TABLA_PREMIO);
        db.execSQL(Constantes.CREAR_TABLA_ALIMENTO);
        db.execSQL(Constantes.CREAR_TABLA_PESEBRERA);
        db.execSQL(Constantes.CREAR_TABLA_SOLICITUD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_EQUINO);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_PREMIOS);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_ALIMENTOS);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_PESEBRERA);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.TABLA_SOLICITUD);
        onCreate(db);
    }

}
