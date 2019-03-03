package com.example.mario.lacrim.Entidades;

public class Alimentos {

    String id_alimento;
    String nombre;
    String descripcion;
    String fecha_ali;
    String id_equino;

    public Alimentos() {
        this.id_alimento = id_alimento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_ali = fecha_ali;
        this.id_equino = id_equino;
    }

    public String getId_alimento() {
        return id_alimento;
    }

    public void setId_alimento(String id_alimento) {
        this.id_alimento = id_alimento;
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
