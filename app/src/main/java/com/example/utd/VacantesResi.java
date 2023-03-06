package com.example.utd;

public class VacantesResi {
    private String uid;
    private String titulo;
    private String carrera;
    private String descripcion;
    private String requisitos;
    private String emailAsociado;




    public VacantesResi(){

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }


    public String getEmailAsociado() {
        return emailAsociado;
    }

    public void setEmailAsociado(String emailAsociado) {
        this.emailAsociado = emailAsociado;
    }



    @Override
    public String toString() {
        return titulo;
    }
}
