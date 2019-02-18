package com.example.mario.lacrim.Utilidades;

public class Constantes {

    public static final String TABLA_USUARIO="usuario";

    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_USER="usuario";
    public static final String CAMPO_CORREO="correo";
    public static final String CAMPO_CONTRASENA="contrasena";

    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " +
            ""+TABLA_USUARIO+" ("+CAMPO_ID+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_USER+" TEXT,"+CAMPO_CORREO+" TEXT,"+CAMPO_CONTRASENA+" TEXT)";



    //Constantes campos tabla equino
    public static final String TABLA_EQUINO="equino";

    public static final String CAMPO_ID_EQUINO="id_equino";
    public static final String CAMPO_NOMBRE_EQUINO="nombre";
    public static final String CAMPO_FECHA_EQUINO="fecha_na";
    public static final String CAMPO_LUGAR_EQUINO="lugar";
    public static final String CAMPO_SEXO_EQUINO="sexo";
    public static final String CAMPO_COLOR_EQUINO="color";
    public static final String CAMPO_MICROCHIP_EQUINO="microchip";
    public static final String CAMPO_CRIADOR_EQUINO="criador";
    public static final String CAMPO_TIPO_EQUINO="tipo";
    public static final String CAMPO_ANDAR_EQUINO="andar";
    public static final String CAMPO_PROPIETARIO_EQUINO="propietario";







    public static final String CREAR_TABLA_EQUINO="CREATE TABLE " +
            ""+TABLA_EQUINO+" ("+CAMPO_ID_EQUINO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_EQUINO+" TEXT, "+CAMPO_FECHA_EQUINO+" TEXT,"+CAMPO_LUGAR_EQUINO+" TEXT,"+CAMPO_SEXO_EQUINO+" TEXT,"+CAMPO_COLOR_EQUINO+" TEXT,"+CAMPO_MICROCHIP_EQUINO+" TEXT,"+CAMPO_CRIADOR_EQUINO+" TEXT,"+CAMPO_TIPO_EQUINO+" TEXT,"+CAMPO_ANDAR_EQUINO+" TEXT,"+CAMPO_PROPIETARIO_EQUINO+" TEXT)";


}
