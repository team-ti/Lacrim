package com.example.mario.lacrim.Utilidades;

public class Constantes {

    public static final String TABLA_USUARIO="usuario";

    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_APELLIDO="apellido";
    public static final String CAMPO_CIUDAD="ciudad";
    public static final String CAMPO_USER="usuario";
    public static final String CAMPO_CORREO="correo";
    public static final String CAMPO_CONTRASENA="contrasena";
    public static final String CAMPO_AVATAR="avatar";

    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " +
            ""+TABLA_USUARIO+" ("+CAMPO_ID+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NOMBRE+" TEXT," +CAMPO_APELLIDO+" TEXT," +CAMPO_CIUDAD+" TEXT," +CAMPO_USER+" TEXT,"+CAMPO_CORREO+" TEXT,"+CAMPO_CONTRASENA+" TEXT,"+CAMPO_AVATAR+" TEXT)";



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
    public static final String CAMPO_ID_USUARIO_EQUINO="id_user";
    public static final String CAMPO_ID_PESEBRERA_EQUINO="id_pesebrera";
    public static final String CAMPO_AVATAR_EQUINO="avatar";
    public static final String CAMPO_VIDEO_EQUINO="video";




    public static final String CREAR_TABLA_EQUINO="CREATE TABLE " +
            ""+TABLA_EQUINO+" ("+CAMPO_ID_EQUINO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_EQUINO+" TEXT, "+CAMPO_FECHA_EQUINO+" TEXT,"+CAMPO_LUGAR_EQUINO+" TEXT,"+
            CAMPO_SEXO_EQUINO+" TEXT,"+CAMPO_COLOR_EQUINO+" TEXT,"+CAMPO_MICROCHIP_EQUINO+" TEXT,"+
            CAMPO_CRIADOR_EQUINO+" TEXT,"+CAMPO_TIPO_EQUINO+" TEXT,"+CAMPO_ANDAR_EQUINO+" TEXT,"+
            CAMPO_PROPIETARIO_EQUINO+" TEXT,"+CAMPO_ID_USUARIO_EQUINO+" INTEGER,"+
            CAMPO_ID_PESEBRERA_EQUINO+" INTEGER,"+CAMPO_AVATAR_EQUINO+" TEXT,"+CAMPO_VIDEO_EQUINO+" TEXT)";



    //Constantes campos tabla premios
    public static final String TABLA_PREMIOS="premio";

    public static final String CAMPO_ID_PREMIO="id_premio";
    public static final String CAMPO_NOMBRE_PREMIO="nombre";
    public static final String CAMPO_DESCRIPCION_PREMIO="descripcion";
    public static final String CAMPO_FECHA_PREMIO="fecha_pre";
    public static final String CAMPO_ID_EQUINO_PREMIO="id_equino";


    public static final String CREAR_TABLA_PREMIO="CREATE TABLE " +
            ""+TABLA_PREMIOS+" ("+CAMPO_ID_PREMIO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_PREMIO+" TEXT, "+CAMPO_DESCRIPCION_PREMIO+" TEXT,"+CAMPO_FECHA_PREMIO+" TEXT,"+CAMPO_ID_EQUINO_PREMIO+" INTEGER)";




    //Constantes campos tabla alimentacion
    public static final String TABLA_ALIMENTOS="alimento";

    public static final String CAMPO_ID_ALIMENTO="id_alimento";
    public static final String CAMPO_NOMBRE_ALIMENTO="nombre";
    public static final String CAMPO_DESCRIPCION_ALIMENTO="descripcion";
    public static final String CAMPO_FECHA_ALIMENTO="fecha_ali";
    public static final String CAMPO_ID_EQUINO_ALIMENTO="id_equino";


    public static final String CREAR_TABLA_ALIMENTO="CREATE TABLE " +
            ""+TABLA_ALIMENTOS+" ("+CAMPO_ID_ALIMENTO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_ALIMENTO+" TEXT, "+CAMPO_DESCRIPCION_ALIMENTO+" TEXT,"+CAMPO_FECHA_ALIMENTO+" TEXT,"+CAMPO_ID_EQUINO_ALIMENTO+" INTEGER)";


    //Constantes campos tabla pesebrera
    public static final String TABLA_PESEBRERA="pesebrera";

    public static final String CAMPO_ID_PESEBRERA="id_pesebrera";
    public static final String CAMPO_NOMBRE_PESEBRERA="nombre";
    public static final String CAMPO_ENCARGADO_PESEBRERA="encargado";
    public static final String CAMPO_CIUDAD_PESEBRERA="ciudad";
    public static final String CAMPO_TELEFONO_PESEBRERA="telefono";
    public static final String CAMPO_ID_USUARIO_PESEBRERA="id_user";


    public static final String CREAR_TABLA_PESEBRERA="CREATE TABLE " +
            ""+TABLA_PESEBRERA+" ("+CAMPO_ID_PESEBRERA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_PESEBRERA+" TEXT, "+CAMPO_ENCARGADO_PESEBRERA+" TEXT,"+CAMPO_CIUDAD_PESEBRERA+" TEXT,"+CAMPO_TELEFONO_PESEBRERA+" TEXT,"+CAMPO_ID_USUARIO_PESEBRERA+" INTEGER)";



    //Constantes campos tabla solicitud
    public static final String TABLA_SOLICITUD="solicitud";

    public static final String CAMPO_ID_SOLICITUD="id_solicitud";
    public static final String CAMPO_ID_EQUINO_SOLICITUD="id_equino";
    public static final String CAMPO_ID_USER_SOLICITUD="id_user";
    public static final String CAMPO_ID_PESEBRERA_SOLICITUD="id_pesebrera";
    public static final String CAMPO_NOMBRE_USER_SOLICITUD="nombre_pesebrera";


    public static final String CREAR_TABLA_SOLICITUD="CREATE TABLE " +
            ""+TABLA_SOLICITUD+" ("+CAMPO_ID_SOLICITUD+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_EQUINO_SOLICITUD+" INTEGER, "+CAMPO_ID_USER_SOLICITUD+" INTEGER, "+CAMPO_ID_PESEBRERA_SOLICITUD+" INTEGER, "+CAMPO_NOMBRE_USER_SOLICITUD+" TEXT)";



}
