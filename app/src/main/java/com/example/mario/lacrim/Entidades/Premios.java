package com.example.mario.lacrim.Entidades;

public class Premios {

    String id_premio;
    String nombre;
    String descripcion;
    String fecha_ali;
    String id_equino;

    public Premios() {
        this.id_premio = id_premio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_ali = fecha_ali;
        this.id_equino = id_equino;
    }


    public String getId_premio() {
        return id_premio;
    }

    public void setId_premio(String id_premio) {
        this.id_premio = id_premio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_ali() {
        return fecha_ali;
    }

    public void setFecha_ali(String fecha_ali) {
        this.fecha_ali = fecha_ali;
    }

    public String getId_equino() {
        return id_equino;
    }

    public void setId_equino(String id_equino) {
        this.id_equino = id_equino;
    }
}
