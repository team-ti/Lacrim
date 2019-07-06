package com.example.mario.lacrim.Entidades;

public class Solicitudes {
    int id_solicitud;
    int id_equino;
    int id_pesebrera;
    int id_user;
    String nombre_solicitud;
    int id_user_receptor;

    public Solicitudes() {
        this.id_equino = id_equino;
        this.id_pesebrera = id_pesebrera;
        this.id_user = id_user;
        this.nombre_solicitud = nombre_solicitud;
        this.id_user_receptor = id_user_receptor;
        this.id_solicitud = id_solicitud;
    }

    public int getId_equino() {
        return id_equino;
    }

    public void setId_equino(int id_equino) {
        this.id_equino = id_equino;
    }

    public int getId_pesebrera() {
        return id_pesebrera;
    }

    public void setId_pesebrera(int id_pesebrera) {
        this.id_pesebrera = id_pesebrera;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNombre_solicitud() {
        return nombre_solicitud;
    }

    public void setNombre_solicitud(String nombre_solicitud) {
        this.nombre_solicitud = nombre_solicitud;
    }

    public int getId_user_receptor() {
        return id_user_receptor;
    }

    public void setId_user_receptor(int id_user_receptor) {
        this.id_user_receptor = id_user_receptor;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }
}
